package rs.edu.raf.exchangeservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.bank.CompanyOtcDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.UserOtcDto;
import rs.edu.raf.exchangeservice.domain.dto.contract.ContractAnswerDto;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.enums.SellerCertificate;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.Contract;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.repository.ContractRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final MyStockService myStockService;
    private final BankServiceClient bankServiceClient;

    //ugovori koje supervizor nije obradio
    //TODO ovo treba da vrati sve ugovore koje supervisor nije obradio i koji su prihvaceni od strane kompanija ili korisnika
    public List<Contract> getAllUnresolvedContracts(){
        return this.contractRepository.findBySellerCertificateAndBankCertificate(SellerCertificate.ACCEPTED, BankCertificate.PROCESSING);
    }


    public boolean companyAccept(ContractAnswerDto dto){
        Contract contract = contractRepository.findById(dto.getContractId()).orElseThrow(() -> new RuntimeException("Contract not found"));
        contract.setSellerCertificate(SellerCertificate.ACCEPTED);
        contract.setComment(dto.getComment());
        contractRepository.save(contract);
        return true;
    }
    public boolean companyDecline(ContractAnswerDto dto){
        Contract contract = contractRepository.findById(dto.getContractId()).orElseThrow(() -> new RuntimeException("Contract not found"));
        contract.setSellerCertificate(SellerCertificate.DECLINED);
        contract.setComment(dto.getComment());
        contractRepository.save(contract);
        return true;
    }


    public synchronized boolean supervisorAccept(ContractAnswerDto dto){

        Contract contract = contractRepository.findById(dto.getContractId()).orElseThrow(() -> new RuntimeException("Contract not found"));


        if(contract.getBankCertificate() == BankCertificate.ACCEPTED){//ako je neki supervizor odgovorio na ugovor
            return true;
        }
        //supervizor zeli da prihvati ugovor koji je vec odbijen
        if(contract.getBankCertificate() == BankCertificate.DECLINED){
            return false;
        }

        contract.setBankCertificate(BankCertificate.ACCEPTED);
        contract.setComment(dto.getComment());

        //TODO: salje se dto na bank servise sa 2 user id-a i iznosom ili 2 company id-a i iznosom i mark racuna (mislim da je RSD za otc uvek)

        Double price =  contract.getPrice().doubleValue() / contract.getAmount();

        myStockService.addAmountToMyStock(contract.getTicker(), contract.getAmount(),contract.getUserBuyerId(), contract.getCompanyBuyerId(), price);
        myStockService.removeAmountFromMyStock(contract.getTicker(), contract.getAmount(),contract.getUserSellerId(), contract.getCompanySellerId());

        //racunamo porez na dobit
        myStockService.calculateTaxForSellStock(contract.getCompanySellerId(), contract.getUserSellerId(), contract.getTicker(), contract.getAmount(), price);

        if(contract.getCompanySellerId() != null) {
            CompanyOtcDto companyOtcDto = new CompanyOtcDto();
            companyOtcDto.setAmount(contract.getPrice().doubleValue());
            companyOtcDto.setCompanyFromId(contract.getCompanyBuyerId());
            companyOtcDto.setCompanyToId(contract.getCompanySellerId());
            bankServiceClient.otcBankTransaction(companyOtcDto);
        }else if(contract.getUserSellerId() != null){
            UserOtcDto userOtcDto = new UserOtcDto();
            userOtcDto.setAmount(contract.getPrice().doubleValue());
            userOtcDto.setUserFromId(contract.getUserBuyerId());
            userOtcDto.setUserToId(contract.getUserSellerId());
            bankServiceClient.otcUserTransaction(userOtcDto);
        }

        contractRepository.save(contract);
        return true;
    }

    public synchronized boolean supervisorDecline(ContractAnswerDto dto){

        Contract contract = contractRepository.findById(dto.getContractId()).orElseThrow(() -> new RuntimeException("Contract not found"));

        if(contract.getBankCertificate() == BankCertificate.DECLINED){//ako je neki supervizor odgovorio na ugovor
            return true;
        }
        //supervizor zeli da odbije ugovor koji je prihvacen
        if(contract.getBankCertificate() == BankCertificate.ACCEPTED){//ako je neki supervizor odgovorio na ugovor
            return false;
        }
        contract.setBankCertificate(BankCertificate.DECLINED);
        contract.setComment(dto.getComment());
        contractRepository.save(contract);
        return true;
    }

    public List<Contract> getAllSentContractsByCompanyId(Long id){
        return this.contractRepository.findByCompanyBuyerId(id);
    }
    public List<Contract> getAllReceivedContractsByCompanyId(Long id){
        return this.contractRepository.findByCompanySellerId(id);
    }

    public List<Contract> getAllSentContractsByUserId(Long id){
        return this.contractRepository.findByUserBuyerId(id);
    }

    public List<Contract> getAllReceivedContractsByUserId(Long id){
        return this.contractRepository.findByUserSellerId(id);
    }

    public List<Contract>getAllByCompanyId(Long id){
        return this.contractRepository.findByCompanySellerIdOrCompanyBuyerId(id, id);
    }







}
