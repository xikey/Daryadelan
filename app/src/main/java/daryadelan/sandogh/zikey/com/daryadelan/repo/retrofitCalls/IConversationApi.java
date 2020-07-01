package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

import daryadelan.sandogh.zikey.com.daryadelan.model.ConversationTopic;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationTopicWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IConversationApi {

    @GET("api/Conversation/getConversation")
    Call<ConversationWrapper> getConversation(@Query("page") int page);

    @POST("api/Conversation/insertConversation")
    Call<ConversationTopicWrapper> insertConversation(@Body ConversationTopic conversationTopic);

    @GET("api/Conversation/getConversationById")
    Call<ConversationWrapper> getConversationById(@Query("id") long id);


}
