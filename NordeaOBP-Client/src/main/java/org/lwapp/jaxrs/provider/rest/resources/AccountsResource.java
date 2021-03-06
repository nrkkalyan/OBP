package org.lwapp.jaxrs.provider.rest.resources;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.lwapp.jaxrs.httpclient.utils.ApacheHttpClient;
import org.lwapp.nordeaobp.psd2.response.ok.GetAccountResponse;
import org.lwapp.nordeaobp.psd2.response.ok.GetAccountTransactionsResponse;
import org.lwapp.nordeaobp.psd2.response.ok.GetAccountsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("aisp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
//@Interceptors({ AuditLogInterceptor.class })
public class AccountsResource {

    //TODO: Read the url from database depending on the environment
    public static final String NORDEAOBP_V1_ACCOUNTS_BASE_URL = "https://api.nordeaopenbanking.com/v1/accounts/";

    private static final Logger LOG = LoggerFactory.getLogger(AccountsResource.class);

    @Context
    private UriInfo uriInfo;
    @EJB
    private ApacheHttpClient apacheHttpClient;

    @GET
    public String ping() {
        return "System is ALIVE :" + System.currentTimeMillis();
    }

    @GET
    @Path("accounts")
    public Response getAccounts(@QueryParam("bankName") final String bankName) throws Exception {
        return this.apacheHttpClient.get(NORDEAOBP_V1_ACCOUNTS_BASE_URL, GetAccountsResponse.class);
    }

    @GET
    @Path("accounts/{accountId}")
    public Response getAccountsById(@PathParam("accountId") final String accountId, @QueryParam("bankName") final String bankName) throws Exception {
        final String target = AccountsResource.NORDEAOBP_V1_ACCOUNTS_BASE_URL + accountId;
        return this.apacheHttpClient.get(target, GetAccountResponse.class);
    }

    @GET
    @Path("accounts/{accountId}/transactions")
    public Response getTransactionsByAccountId(@PathParam("accountId") final String accountId, @QueryParam("bankName") final String bankName) throws Exception {
        final String target = AccountsResource.NORDEAOBP_V1_ACCOUNTS_BASE_URL + accountId + "/transactions";
        return this.apacheHttpClient.get(target, GetAccountTransactionsResponse.class);
    }

    @GET
    @Path("banks")
    public Response getBanks() throws Exception {
        final List<String> banks = Stream.of("Nordea", "Swedbank").collect(Collectors.toList());
        return Response.ok(banks, MediaType.APPLICATION_JSON).build();
    }

}
