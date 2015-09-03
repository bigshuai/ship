package org.ship.shipservice.worker;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component  
public class SyncCouponStatusWorker {
	@Scheduled(cron="0/5 * * * * ? ") //间隔5秒执行  
	public void process(){  
//        System.out.println("process.....");  
    }  
}
