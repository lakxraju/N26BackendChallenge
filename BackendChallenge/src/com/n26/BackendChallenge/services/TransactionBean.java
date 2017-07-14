package com.n26.BackendChallenge.services;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransactionBean extends Object implements Comparable<TransactionBean>{
		@XmlElement(required=true)
	    private volatile Double amount;
		@XmlElement(required=true)
	    private volatile Long timestamp;
	    private final ReentrantLock lock = new ReentrantLock(true);
	    private final Condition notEmpty = lock.newCondition();
	    
	    public TransactionBean(){
	    	
	    }
	    
		public synchronized Double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			ReentrantLock lock_inner = lock;
			try{
				lock_inner.lock();
				this.amount = amount;
				notEmpty.signal();
			}finally{
				lock_inner.unlock();
			}
		}
		public synchronized Long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			ReentrantLock lock_inner = lock;
			try{
				lock_inner.lock();
				this.timestamp = timestamp;
				notEmpty.signal();
			}finally{
				lock_inner.unlock();
			}
		}

		@Override
		public int compareTo(TransactionBean inputBean) {
			if(this.getTimestamp() == inputBean.getTimestamp())
				return 0;
			return (this.getTimestamp() - inputBean.getTimestamp() > 0)?1:-1;
		} 

		@Override
		public boolean equals(Object inputBean){
			if(!(inputBean instanceof TransactionBean))
			{
				return false;
			}
			else if((((TransactionBean)inputBean).getTimestamp() == this.getTimestamp())&&(((TransactionBean)inputBean).getAmount() == this.getAmount()))
			{
				return true;
			}
			return false;
		}
		
		@Override
		public int hashCode()
		{
			return Objects.hash(this.getAmount(),this.getTimestamp());
		}
		
		@Override
		public String toString()
		{
			return "Timestamp:  " + Double.toString(this.getTimestamp())+ "  Amount:  " +Double.toString(this.getAmount());
		}
}
