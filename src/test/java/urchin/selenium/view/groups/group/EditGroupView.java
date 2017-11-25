package urchin.selenium.view.groups.group;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.TestComponent;
import urchin.selenium.view.PageView;

@TestComponent
public class EditGroupView extends PageView {

    @Override
    public EditGroupView verifyAtView() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id("edit-group")));
        return this;
    }

    public void clickDeleteGroupButton() {
        driver.findElement(By.id("edit-group__delete-btn")).click();
    }
}
