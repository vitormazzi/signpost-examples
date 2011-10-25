from  java.io import BufferedReader;
from  java.io import InputStreamReader;
from  java.net import HttpURLConnection;
from  java.net import URL;

from  oauth.signpost import OAuth;
from  oauth.signpost import OAuthConsumer;
from  oauth.signpost import OAuthProvider;
from  oauth.signpost.basic import DefaultOAuthConsumer;
from  oauth.signpost.basic import DefaultOAuthProvider;

CONSUMER_KEY = "8ab29iwKFI";
CONSUMER_SECRET = "VnWYenOqYsHtcFowrdJlwdJNALq5Go9v";

CALLBACK_URL = OAuth.OUT_OF_BAND;
REQUEST_TOKEN_URL = "http://sandbox.app.passaporteweb.com.br/sso/initiate";
ACCESS_TOKEN_URL = "http://sandbox.app.passaporteweb.com.br/sso/token";
AUTHORIZE_URL = "http://sandbox.app.passaporteweb.com.br/sso/authorize";
USER_DATA = "http://sandbox.app.passaporteweb.com.br/sso/fetchuserdata";

class PassaporteWebMain():

    def call(self):

        consumer = DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

        provider = DefaultOAuthProvider(
            REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL
        );

        print "Fetching request token from PassaporteWeb..."

        authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
        authUrl = OAuth.addQueryParameters(authUrl, OAuth.OAUTH_CONSUMER_KEY, CONSUMER_KEY);

        print "Request token: ", consumer.getToken()
        print "Token secret: ", consumer.getTokenSecret()

        print "Now visit:\n", authUrl, "\n... and grant this app authorization"
        print "After granting authorization, you should be redirected to the callback url"

        pin = raw_input("Enter the PIN code and hit ENTER when you're done:")

        print "Fetching access token from PassaporteWeb..."

        provider.retrieveAccessToken(consumer, pin);

        print "Access token: ", consumer.getToken()
        print "Token secret: ", consumer.getTokenSecret()

        url = URL(USER_DATA);
        request = url.openConnection();

        consumer.sign(request);

        print "Sending request..."
        request.connect();

        print "Response: %d %s" % (request.getResponseCode(), request.getResponseMessage())


if __name__ == '__main__':

    myfc_id = PassaporteWebMain()
    myfc_id.call()
