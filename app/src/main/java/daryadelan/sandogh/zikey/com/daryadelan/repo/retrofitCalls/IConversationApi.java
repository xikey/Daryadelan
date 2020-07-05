package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

import daryadelan.sandogh.zikey.com.daryadelan.model.ConversationTopic;
import daryadelan.sandogh.zikey.com.daryadelan.model.Message;
import daryadelan.sandogh.zikey.com.daryadelan.model.SendMessage;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationTopicWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.MessageWrapper;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IConversationApi {

    @GET("api/Conversation/getConversation")
    Call<ConversationWrapper> getConversation(@Query("page") int page);


    @POST("api/Conversation/insertConversation")
    Call<ConversationTopicWrapper> insertConversation(@Body ConversationTopic conversationTopic);


    @POST("api/Conversation/insertMessage")
    Call<MessageWrapper> insertMessage(@Body SendMessage message);


    @GET("api/Conversation/{id}")
    Call<ConversationWrapper> getConversationById(@Path("id") long id);


}
