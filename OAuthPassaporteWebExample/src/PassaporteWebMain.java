import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

public class PassaporteWebMain {

    public static final String CONSUMER_KEY = "8ab29iwKFI";
    public static final String CONSUMER_SECRET = "VnWYenOqYsHtcFowrdJlwdJNALq5Go9v";

    public static final String CALLBACK_URL = OAuth.OUT_OF_BAND;
    public static final String REQUEST_TOKEN_URL = "http://sandbox.app.passaporteweb.com.br/sso/initiate";
    public static final String ACCESS_TOKEN_URL = "http://sandbox.app.passaporteweb.com.br/sso/token";
    public static final String AUTHORIZE_URL = "http://sandbox.app.passaporteweb.com.br/sso/authorize";
    public static final String USER_DATA = "http://sandbox.app.passaporteweb.com.br/sso/fetchuserdata";

    public static void main(String[] args) throws Exception {

        OAuthConsumer consumer = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

        OAuthProvider provider = new DefaultOAuthProvider(REQUEST_TOKEN_URL,
            ACCESS_TOKEN_URL, AUTHORIZE_URL);

        System.out.println("Fetching request token from PassaporteWeb...");

        String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
        authUrl = OAuth.addQueryParameters(authUrl, OAuth.OAUTH_CONSUMER_KEY, CONSUMER_KEY);

        System.out.println("Request token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());

        System.out.println("Now visit:\n" + authUrl + "\n... and grant this app authorization");
        System.out.println("Enter the PIN code and hit ENTER when you're done:");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String pin = br.readLine();

        System.out.println("Fetching access token from PassaporteWeb...");

        provider.retrieveAccessToken(consumer, pin);

        System.out.println("Access token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());

        URL url = new URL(USER_DATA);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        consumer.sign(request);

        System.out.println("Sending request...");
        request.connect();

        System.out.println("Response: " + request.getResponseCode() + " "
                + request.getResponseMessage());

    }
}
