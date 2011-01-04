from  java.io import BufferedReader;
from  java.io import InputStreamReader;
from  java.net import HttpURLConnection;
from  java.net import URL;

from  oauth.signpost import OAuth;
from  oauth.signpost import OAuthConsumer;
from  oauth.signpost import OAuthProvider;
from  oauth.signpost.basic import DefaultOAuthConsumer;
from  oauth.signpost.basic import DefaultOAuthProvider;

CONSUMER_KEY = "uam9fsma9a87w7jm3v2r9n47";
CONSUMER_SECRET = "Uze4zNHbJM";

CALLBACK_URL = "http://localhost/sso/callback";
MYFCID_REQUEST_TOKEN_URL = "http://sandbox.id.myfreecomm.com.br/sso/initiate";
MYFCID_ACCESS_TOKEN_URL = "http://sandbox.id.myfreecomm.com.br/sso/token";
MYFCID_AUTHORIZE_URL = "http://sandbox.id.myfreecomm.com.br/sso/authorize";
MYFCID_USER_DATA = "http://sandbox.id.myfreecomm.com.br/sso/fetchuserdata";

class MyfcIDMain():

    def call(self):

        consumer = DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

        provider = DefaultOAuthProvider(
            MYFCID_REQUEST_TOKEN_URL, MYFCID_ACCESS_TOKEN_URL, MYFCID_AUTHORIZE_URL
        );

        print "Fetching request token from MyfcID..."

        authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
        authUrl = OAuth.addQueryParameters(authUrl, OAuth.OAUTH_CONSUMER_KEY, CONSUMER_KEY);

        print "Request token: ", consumer.getToken()
        print "Token secret: ", consumer.getTokenSecret()

        print "Now visit:\n", authUrl, "\n... and grant this app authorization"

        pin = raw_input("Enter the PIN code and hit ENTER when you're done:")

        print "Fetching access token from MyfcID..."

        provider.retrieveAccessToken(consumer, pin);

        print "Access token: ", consumer.getToken()
        print "Token secret: ", consumer.getTokenSecret()

        url = URL(MYFCID_USER_DATA);
        request = url.openConnection();

        consumer.sign(request);

        print "Sending request..."
        request.connect();

        print "Response: %d %s" % (request.getResponseCode(), request.getResponseMessage())


if __name__ == '__main__':

    myfc_id = MyfcIDMain()
    myfc_id.call()
