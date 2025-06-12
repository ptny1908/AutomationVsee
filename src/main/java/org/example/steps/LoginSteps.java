package org.example.steps;

import io.cucumber.java.en.Then;
import org.example.screenAction.CustomerPage;
import org.example.screenAction.ProviderPage;
import utils.Configuration;

public class LoginSteps {
    String baseUrl = Configuration.get().getTestProperty("baseURL");
    @Then("I open Customer and Provider page")
    public void navigateToPages() {
        CustomerPage.navigateTo(baseUrl);
        ProviderPage.navigateTo(baseUrl);
       System.out.println("test");
    }
    @Then("^I input custom page infor with name (.*) and Submit")
    public void inputCustomerPage(String name) throws InterruptedException {
        CustomerPage.submitCustomerRequest(name);
    }

    @Then("^I input provider with username (.*) and password (.*)")
    public void loginProviderPage(String username,String password) throws InterruptedException {
        ProviderPage.login(username,password);
    }
    @Then("^I select Call button at (.*) customer")
    public void selectCallButton(String name) throws InterruptedException {
        ProviderPage.call(name);
    }
    @Then("^Provider end call")
    public void providerEndCall() throws InterruptedException {
        ProviderPage.endCall();
    }

    @Then("^Customer end call")
    public void customerEndCall() throws InterruptedException {
        CustomerPage.endCall();
    }
    @Then("^I select chat icon at (.*) customer")
    public void selectChatIcon(String name) throws InterruptedException {
        ProviderPage.chat(name);
    }

    @Then("^I type (.*) sentence")
    public void typeMessage(String message) throws InterruptedException {
        ProviderPage.typeMessage(message);
    }

    @Then("^I verify customer receive (.*) sentence")
    public void verifyMessage(String message) throws InterruptedException {
       CustomerPage.verifyMessageFromProvider(message);
    }
}
