package com.smallworld;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TransactionDataFetcher {

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount( List<Transaction> Tlist) {
        int i;
        double sum=0;
        for(i=0;i<Tlist.size();i++)
        {
            sum=sum+Double.parseDouble(Tlist.get(i).amount);
        }
        return sum;
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName,List<Transaction> Tlist) {
        
        int i;
        double sum=0;
        for(i=0;i<Tlist.size();i++)
        {
            if(senderFullName.equals(Tlist.get(i).senderFullName))
            sum=sum+Double.parseDouble(Tlist.get(i).amount);
        }
        
        return sum;
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount(List<Transaction> Tlist) {
        int i=0;
        double max=Double.parseDouble(Tlist.get(0).amount);
        
        for(i=1;i<Tlist.size();i++)
        {
            if(Double.parseDouble(Tlist.get(i).amount)>max)
            {
                max=Double.parseDouble(Tlist.get(i).amount);
            }
        }
        return max;
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients(List<Transaction> Tlist) {
        
        int i=0;
        List<String> UniqueClients = new ArrayList<String>();
        for(i=0;i<Tlist.size();i++)
        {
            if(!UniqueClients.contains(Tlist.get(i).senderFullName)){
                UniqueClients.add(Tlist.get(i).senderFullName);
            }
            if(!UniqueClients.contains(Tlist.get(i).beneficiaryFullName)){
                UniqueClients.add(Tlist.get(i).beneficiaryFullName);
            }
        }
        return UniqueClients.size() + 1;
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName,List<Transaction> Tlist) {
        for(int i=0; i < Tlist.size(); i++)
        {
            if((clientFullName.equals(Tlist.get(i).senderFullName) || clientFullName.equals(Tlist.get(i).beneficiaryFullName)) && Tlist.get(i).issueSolved == "false")
                return true;
        }
        
        return false;
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds(List<Transaction> Tlist) {
        
        Set<Integer> UnsolvedIssueIds = new HashSet<Integer>();
        for(int i=0; i < Tlist.size(); i++)
        {
            if(Tlist.get(i).issueSolved.equals("false"))
            {
                UnsolvedIssueIds.add(Integer.parseInt((Tlist.get(i).mtn)));
            }
        }
        
        return UnsolvedIssueIds;
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages(List<Transaction> Tlist) {
        
 
        ArrayList<String> SolvedIssue = new ArrayList<String>();
        for(int i=0; i < Tlist.size(); i++)
        {
            if(Tlist.get(i).issueSolved.equals("true")&& !(Tlist.get(i).issueMessage.isEmpty()))
               SolvedIssue.add(String.valueOf(Tlist.get(i)));
                
        }
        
       return SolvedIssue;
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount(List<Transaction> Tlist) {
        
        Collections.sort(Tlist, new Comparator<Transaction>() {
            
            public int compare(Transaction t1, Transaction t2) 
            
            {
                return Double.compare(Double.parseDouble(t2.amount), Double.parseDouble(t1.amount));
            }
        });
        
            return Tlist.subList(0, Math.min(3, Tlist.size()));
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender(List<Transaction> Tlist) {
        
        Map<String, Double> senderToTotalAmount = new HashMap<>();

        // Calculate the total amount sent by each sender
        for (Transaction transaction : Tlist) {
            String senderFullName = transaction.senderFullName;
            double amount = Double.parseDouble(transaction.amount);

            // Add the amount to the total for this sender
            senderToTotalAmount.put(senderFullName, senderToTotalAmount.getOrDefault(senderFullName, 0.0) + amount);
        }

        // Find the sender with the highest total amount
        Optional<Map.Entry<String, Double>> topSenderEntry = senderToTotalAmount.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));

        // Return the full name of the top sender if found
        return topSenderEntry.map(Map.Entry::getKey);
    }
  
    public void fetchData() throws IOException, ParseException{
        
        
        
        JSONParser parser = new JSONParser();

        try {     
            Object obj = parser.parse(new FileReader("d:\\transactions.json"));

            JSONObject jsonObject =  (JSONObject) obj;
            String testJson ="";
            ObjectMapper mapper = new ObjectMapper();
            List<Transaction> Tlist =  mapper.readValue(testJson,  new TypeReference<List<Transaction>>(){});

            }
          catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        
    }
    
    

}
