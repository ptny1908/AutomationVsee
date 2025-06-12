package org.example.steps;


import io.cucumber.java.en.Then;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.example.github.GithubApiHelper;
import org.example.github.Repository;

import java.util.Comparator;
import java.util.List;

import static org.example.github.GithubApiHelper.calculateTotalOpenIssues;

public class GithubSteps {
    @Then("I call Github Api to get total open issues")
    public void getTotalOpenIssues() {
        GithubApiHelper githubApiHelper = new GithubApiHelper();

        try (CloseableHttpResponse reponse = githubApiHelper.getApiResponse()) {
            // Get list of repository
            List<Repository> repositories = null;
                repositories = GithubApiHelper.getRepositories(reponse);
                int totalOpenIssues = calculateTotalOpenIssues(repositories);
                System.out.println("The total of open issues: " + totalOpenIssues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("I call Github Api to get most watched repository")
    public void findMostWatchedRepository() {
        GithubApiHelper githubApiHelper = new GithubApiHelper();

        try (CloseableHttpResponse reponse = githubApiHelper.getApiResponse()) {
            // Get list of repositories
            List<Repository> repositories = null;
            repositories = GithubApiHelper.getRepositories(reponse);
            Repository mostWatchedRepo = GithubApiHelper.findMostWatchedRepository(repositories);
            System.out.println("\nRepository with the most watchers " + mostWatchedRepo.getName() +
                    " (Watchers: " + mostWatchedRepo.getWatchers() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Then("I call Github Api to get repositories as updated descending sort")
    public void descendingSort() {
        GithubApiHelper githubApiHelper = new GithubApiHelper();
        try (CloseableHttpResponse reponse = githubApiHelper.getApiResponse()) {
            // Get list of repository
            List<Repository> repositories = null;
            repositories = GithubApiHelper.getRepositories(reponse);
            repositories.sort(Comparator.comparing(Repository::getUpdatedAt, Comparator.reverseOrder()));
            System.out.println("\nRepositories in descending date order:");
            repositories.forEach(repo -> System.out.println(repo.getName() + " - Updated: " + repo.getUpdatedAt()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
