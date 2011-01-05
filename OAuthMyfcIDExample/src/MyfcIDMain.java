import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

public class MyfcIDMain {

    public static final String CONSUMER_KEY = "uam9fsma9a87w7jm3v2r9n47";
    public static final String CONSUMER_SECRET = "Uze4zNHbJM";

    public static final String CALLBACK_URL = OAuth.OUT_OF_BAND;
    public static final String MYFCID_REQUEST_TOKEN_URL = "http://sandbox.id.myfreecomm.com.br/sso/initiate";
    public static final String MYFCID_ACCESS_TOKEN_URL = "http://sandbox.id.myfreecomm.com.br/sso/token";
    public static final String MYFCID_AUTHORIZE_URL = "http://sandbox.id.myfreecomm.com.br/sso/authorize";
    public static final String MYFCID_USER_DATA = "http://sandbox.id.myfreecomm.com.br/sso/fetchuserdata";

    public static void main(String[] args) throws Exception {

        OAuthConsumer consumer = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

        OAuthProvider provider = new DefaultOAuthProvider(MYFCID_REQUEST_TOKEN_URL,
            MYFCID_ACCESS_TOKEN_URL, MYFCID_AUTHORIZE_URL);

        System.out.println("Fetching request token from MyfcID...");

        String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
        authUrl = OAuth.addQueryParameters(authUrl, OAuth.OAUTH_CONSUMER_KEY, CONSUMER_KEY);

        System.out.println("Request token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());

        System.out.println("Now visit:\n" + authUrl + "\n... and grant this app authorization");
        System.out.println("Enter the PIN code and hit ENTER when you're done:");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String pin = br.readLine();

        System.out.println("Fetching access token from MyfcID...");

        provider.retrieveAccessToken(consumer, pin);

        System.out.println("Access token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());

        URL url = new URL(MYFCID_USER_DATA);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        consumer.sign(request);

        System.out.println("Sending request...");
        request.connect();

        System.out.println("Response: " + request.getResponseCode() + " "
                + request.getResponseMessage());

    }
}
