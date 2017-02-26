package urchin.controller;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import urchin.api.FolderDto;
import urchin.api.MountEncryptedFolderDto;
import urchin.api.PassphraseDto;
import urchin.api.VirtualFolderDto;
import urchin.api.support.ResponseMessage;
import urchin.domain.model.EncryptedFolder;
import urchin.testutil.TemporaryFolderUnmount;
import urchin.testutil.TestApplication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.exists;
import static java.util.Arrays.asList;
import static junit.framework.TestCase.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static urchin.domain.util.EncryptedFolderUtil.getEncryptedFolder;

public class FolderControllerIT extends TestApplication {

    private static final String FILENAME = "test_file_for_folder_service_it.txt";
    private static final String FOLDER1_NAME = "/folder1";
    private static final String FOLDER2_NAME = "/folder2";
    private static final String FOLDER_VIRTUAL_NAME = "/virtual";

    private Path folder_1;
    private Path folder_2;
    private EncryptedFolder encryptedFolder_1;
    private EncryptedFolder encryptedFolder_2;
    private Path virtualFolder;
    private String tmpFolderPath;

    @Rule
    public TemporaryFolderUnmount temporaryFolderUnmount = new TemporaryFolderUnmount();

    @Before
    public void setup() {
        tmpFolderPath = temporaryFolderUnmount.getRoot().getAbsolutePath();
        folder_1 = Paths.get(tmpFolderPath + FOLDER1_NAME);
        folder_2 = Paths.get(tmpFolderPath + FOLDER2_NAME);
        encryptedFolder_1 = getEncryptedFolder(folder_1);
        encryptedFolder_2 = getEncryptedFolder(folder_2);
        virtualFolder = Paths.get(tmpFolderPath + FOLDER_VIRTUAL_NAME);
    }

    @Test
    public void createAndMountAndUnmountEncryptedAndVirtualFoldersThatAreSharedAndUnsharedOnNetwork() throws Exception {
        FolderDto encryptedFolderDto = new FolderDto(folder_1.toAbsolutePath().toString());

        //1. create encrypted folder

        ResponseEntity<ResponseMessage<PassphraseDto>> createResponse_1 = postCreateRequest(encryptedFolderDto);

        assertEquals(HttpStatus.OK, createResponse_1.getStatusCode());
        assertNotNull(createResponse_1.getBody().getData().getPassphrase());
        assertTrue(exists(folder_1));
        assertTrue(exists(encryptedFolder_1.getPath()));

        //2. unmount encrypted folder

        ResponseEntity<ResponseMessage<String>> unmountResponse_1 = postUnmountRequest(encryptedFolderDto);

        assertEquals(HttpStatus.OK, unmountResponse_1.getStatusCode());
        assertFalse(exists(folder_1));
        assertTrue(exists(encryptedFolder_1.getPath()));

        //3. mount encrypted folder again

        MountEncryptedFolderDto mountEncryptedFolderDto = new MountEncryptedFolderDto(
                folder_1.toAbsolutePath().toString(),
                createResponse_1.getBody().getData().getPassphrase()
        );

        ResponseEntity<ResponseMessage<String>> mountResponse_1 = postMountRequest(mountEncryptedFolderDto);
        assertEquals(HttpStatus.OK, mountResponse_1.getStatusCode());
        assertTrue(exists(folder_1));
        assertTrue(exists(encryptedFolder_1.getPath()));

        //4. create 2nd encrypted folder

        ResponseEntity<ResponseMessage<PassphraseDto>> createResponse_2 = postCreateRequest(new FolderDto(folder_2.toAbsolutePath().toString()));
        assertEquals(HttpStatus.OK, createResponse_2.getStatusCode());
        assertTrue(exists(folder_2));
        assertTrue(exists(encryptedFolder_2.getPath()));

        //5. setup virtual folder

        VirtualFolderDto virtualFolderDto = new VirtualFolderDto(asList(folder_1.toString(), folder_2.toString()), virtualFolder.toString());

        ResponseEntity<ResponseMessage<String>> virtualFolderResponse = postSetupVirtualFolderRequest(virtualFolderDto);

        assertEquals(HttpStatus.OK, virtualFolderResponse.getStatusCode());
        assertTrue(exists(virtualFolder));

        //.6 create file in virtual folder, which will be located in one of the encrypted folders.

        createFileInFolder(FILENAME, virtualFolder);
        assertTrue(folderContainsFile(virtualFolder, FILENAME));
        assertTrue(folderContainsFile(folder_1, FILENAME) || folderContainsFile(folder_2, FILENAME));

        //7. share virtual folder

        FolderDto shareFolderDto = new FolderDto(virtualFolder.toString());

        ResponseEntity<ResponseMessage<String>> shareFolderResponse = postShareFolderRequest(shareFolderDto);
        assertEquals(HttpStatus.OK, shareFolderResponse.getStatusCode());

        SmbFile sharedFolder = getSmbFile(FOLDER_VIRTUAL_NAME);
        assertEquals(1, sharedFolder.list().length);
        assertEquals(FILENAME, sharedFolder.list()[0]);

        //8. unshare virtual folder

        ResponseEntity<ResponseMessage<String>> unshareFolderResponse = postUnshareFolderRequest(shareFolderDto);
        assertEquals(HttpStatus.OK, unshareFolderResponse.getStatusCode());

        try {
            getSmbFile(FOLDER_VIRTUAL_NAME).list();
            fail("expected listing files in network share to fail because network share should have been removed");
        } catch (SmbException ignore) {
        }

        //9. unmount virtual folder

        ResponseEntity<ResponseMessage<String>> unmountVirtualFOlderResponse = postUnmountVirtualFolderRequest(new FolderDto(virtualFolder.toString()));

        assertEquals(HttpStatus.OK, unmountVirtualFOlderResponse.getStatusCode());
        assertFalse(exists(virtualFolder));
        assertTrue(folderContainsFile(folder_1, FILENAME) || folderContainsFile(folder_2, FILENAME));

    }

    private ResponseEntity<ResponseMessage<PassphraseDto>> postCreateRequest(FolderDto folderDto) {
        return testRestTemplate.exchange(discoverControllerPath() + "/create", HttpMethod.POST, new HttpEntity<>(folderDto), new ParameterizedTypeReference<ResponseMessage<PassphraseDto>>() {
        });
    }

    private ResponseEntity<ResponseMessage<String>> postUnmountRequest(FolderDto folderDto) {
        return testRestTemplate.exchange(discoverControllerPath() + "/unmount", HttpMethod.POST, new HttpEntity<>(folderDto), new ParameterizedTypeReference<ResponseMessage<String>>() {
        });
    }

    private ResponseEntity<ResponseMessage<String>> postMountRequest(MountEncryptedFolderDto mountEncryptedFolderDto) {
        return testRestTemplate.exchange(discoverControllerPath() + "/mount", HttpMethod.POST, new HttpEntity<>(mountEncryptedFolderDto), new ParameterizedTypeReference<ResponseMessage<String>>() {
        });
    }

    private ResponseEntity<ResponseMessage<String>> postSetupVirtualFolderRequest(VirtualFolderDto virtualFolderDto) {
        return testRestTemplate.exchange(discoverControllerPath() + "/virtual/create", HttpMethod.POST, new HttpEntity<>(virtualFolderDto), new ParameterizedTypeReference<ResponseMessage<String>>() {
        });
    }

    private ResponseEntity<ResponseMessage<String>> postUnmountVirtualFolderRequest(FolderDto folderDto) {
        return testRestTemplate.exchange(discoverControllerPath() + "/virtual/unmount", HttpMethod.POST, new HttpEntity<>(folderDto), new ParameterizedTypeReference<ResponseMessage<String>>() {
        });
    }

    private ResponseEntity<ResponseMessage<String>> postShareFolderRequest(FolderDto folderDto) {
        return testRestTemplate.exchange(discoverControllerPath() + "/share", HttpMethod.POST, new HttpEntity<>(folderDto), new ParameterizedTypeReference<ResponseMessage<String>>() {
        });
    }

    private ResponseEntity<ResponseMessage<String>> postUnshareFolderRequest(FolderDto folderDto) {
        return testRestTemplate.exchange(discoverControllerPath() + "/unshare", HttpMethod.POST, new HttpEntity<>(folderDto), new ParameterizedTypeReference<ResponseMessage<String>>() {
        });
    }

    private Path createFileInFolder(String filename, Path folder) throws IOException {
        Path file = Paths.get(folder.toAbsolutePath().toString() + "/" + filename);
        return Files.createFile(file);
    }

    private boolean folderContainsFile(Path folder, String filename) throws IOException {
        for (Path path : Files.newDirectoryStream(folder)) {
            if (path.getFileName().toString().equals(filename)) {
                return true;
            }
        }
        return false;
    }

    private SmbFile getSmbFile(String shareFolderName) throws MalformedURLException {
        return new SmbFile(String.format("smb://127.0.0.1/%s/", shareFolderName));
    }

}