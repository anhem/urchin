package urchin.selenium.view.groups.group;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import urchin.selenium.view.PageView;

public class NewGroupView extends PageView<NewGroupView> {

    @Override
    protected String viewUrl() {
        return "/groups/new";
    }

    @Override
    public NewGroupView verifyAtView() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id("new-group")));
        return this;
    }

    public NewGroupView fillGroupName(String username) {
        driver.findElement(By.name("groupName")).sendKeys(username);
        return this;
    }

    public void clickCreateGroupButton() {
        driver.findElement(By.id("new-group__create-btn")).click();
    }

    public void clickCancelButton() {
        driver.findElement(By.id("new-group__cancel-btn")).click();
    }
}
