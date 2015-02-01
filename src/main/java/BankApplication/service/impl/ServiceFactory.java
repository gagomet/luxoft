package BankApplication.service.impl;

import BankApplication.service.AccountService;
import BankApplication.service.BankService;
import BankApplication.service.ClientService;

/**
 * Created by Padonag on 01.02.2015.
 */
public class ServiceFactory {
    public static AccountService getAccountService(){
        return AccountServiceImpl.getInstance();
    }

    public static ClientService getClientService(){
        return ClientServiceImpl.getInstance();
    }

    public static BankService getBankService(){
        return BankServiceImpl.getInstance();
    }
}
