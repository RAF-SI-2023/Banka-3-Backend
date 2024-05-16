package rs.edu.raf.exchangeservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.dto.contract.ContractAnswerDto;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.enums.SellerCertificate;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.Contract;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.repository.ContractRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final TickerRepository tickerRepository;
    private final MyStockRepository myStockRepository;

    //ugovori koje supervizor nije obradio
    public List<Contract> getAllUnresolvedContracts(){
        return this.contractRepository.findByBankCertificate(BankCertificate.PROCESSING);
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

        //TODO ostalo je da se skine novac sa racuna jedne firme i doda na racun druge firme,kaze ogi da ostavimo straletu to

        //Tek kada supervizor odobri, treba da se kreira novi MyStock
        MyStock myStock = new MyStock();
        myStock.setTicker(contract.getTicker());
        myStock.setCompanyId(contract.getCompanyBuyerId());
        myStock.setAmount(contract.getAmount());
        Ticker ticker = tickerRepository.findByTicker(contract.getTicker());

        myStock.setCurrencyMark(ticker.getCurrencyName());
        myStock.setPrivateAmount(contract.getAmount());
        myStock.setPublicAmount(contract.getAmount());
        myStock.setUserId(0L);

        myStockRepository.save(myStock);
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

    public List<Contract>getAllByCompanyId(Long id){
        return this.contractRepository.findByCompanySellerIdOrCompanyBuyerId(id);
    }







}
