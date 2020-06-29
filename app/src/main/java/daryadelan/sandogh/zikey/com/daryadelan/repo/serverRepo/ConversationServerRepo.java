package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.ConversationTopic;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ServerWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IConversation;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.ICampApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.IConversationApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationServerRepo implements IConversation {

    Call<ConversationWrapper> call;

    @Override
    public void getAllConversationsTopics(Context context, String tokenType, String token, IRepoCallBack<ConversationWrapper> callBack) {

        IConversationApi campApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(IConversationApi.class);
        call = campApi.getConversation();
        call.enqueue(new Callback<ConversationWrapper>() {
            @Override
            public void onResponse(Call<ConversationWrapper> call, Response<ConversationWrapper> response) {

                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("اطلاعات جهت نمایش وجود ندارد"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                if (response.body().getData() == null || response.body().getData().size() == 0) {
                    callBack.onError(new Throwable("اطلاعات جهت نمایش وجود ندارد"));
                    return;
                }

                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<ConversationWrapper> call, Throwable throwable) {

                callBack.onError(new Throwable("اطلاعات جهت نمایش وجود ندارد"));
                return;
            }
        });
    }

    @Override
    public void insertConversationTopic(Context context, String tokenType, String token, ConversationTopic conversationTopic, IRepoCallBack<ServerWrapper> callBack) {

    }
}
