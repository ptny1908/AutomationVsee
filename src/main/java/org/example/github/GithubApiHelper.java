package org.example.github;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GithubApiHelper {
    private static final String GITHUB_API_BASE = "https://api.github.com";
    private static final String ORGANIZATION = "SeleniumHQ";
    private static final String AUTH_TOKEN = ""; // Github token
    private static final Gson gson = new Gson();
    public CloseableHttpResponse getApiResponse() throws IOException {
        String url = GITHUB_API_BASE + "/orgs/" + ORGANIZATION + "/repos";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
//            request.setHeader("Authorization", "token " + AUTH_TOKEN);
        request.setHeader("Accept", "application/vnd.github.v3+json");
        return httpClient.execute(request);
    }

    public static List<Repository> getRepositories(CloseableHttpResponse response) throws IOException, ParseException {
        List<Repository> repos = new ArrayList<>();
            String json = EntityUtils.toString(response.getEntity());

        try {
            //mapping json string to list of repository
            Type repositoryListType = new TypeToken<ArrayList<Repository>>() {}.getType();
            repos = gson.fromJson(json, repositoryListType);

        } catch (JsonSyntaxException e) {
            e.printStackTrace();

        }
        return repos;
    }

    public static int calculateTotalOpenIssues(List<Repository> repositories) {
        return repositories.stream().mapToInt(Repository::getOpen_issues_count).sum();
    }

    public static Repository findMostWatchedRepository(List<Repository> repositories) {
        return repositories.stream().max(Comparator.comparingInt(Repository::getWatchers)).orElse(null);
    }
}
