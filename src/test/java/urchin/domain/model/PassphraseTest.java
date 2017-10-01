package urchin.domain.model;

import org.junit.Test;

public class PassphraseTest {

    @Test(expected = IllegalArgumentException.class)
    public void emptyPassphraseThrowsException() {
        ImmutablePassphrase.of("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooShortPassphraseThrowsException() {
        ImmutablePassphrase.of("short");
    }

    @Test
    public void passphraseIsOfCorrectLength() {
        StringBuilder passphrase = new StringBuilder();
        for (int i = 0; i < Passphrase.ECRYPTFS_MAX_PASSPHRASE_LENGTH; i++) {
            passphrase.append("X");
        }

        ImmutablePassphrase.of(passphrase.toString());
    }

}