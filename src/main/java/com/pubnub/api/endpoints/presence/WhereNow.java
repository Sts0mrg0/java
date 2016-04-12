package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.core.models.WhereNowData;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;


@Builder
public class WhereNow extends Endpoint<Envelope<WhereNowData>, WhereNowData> {

    private Pubnub pubnub;
    private String uuid;


    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<Envelope<WhereNowData>> doWork() {
        PresenceService service = this.createRetrofit(pubnub).create(PresenceService.class);
        return service.whereNow(pubnub.getConfiguration().getSubscribeKey(),
                this.uuid != null ? this.uuid : pubnub.getConfiguration().getUuid());
    }

    @Override
    protected WhereNowData createResponse(Response<Envelope<WhereNowData>> input) throws PubnubException {
        if (input.body() == null || input.body().getPayload() == null) {
            throw new PubnubException(PubnubError.PNERROBJ_PARSING_ERROR);
        }

        return input.body().getPayload();
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNWhereNowOperation;
    }

}