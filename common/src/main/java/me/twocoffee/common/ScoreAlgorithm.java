package me.twocoffee.common;

import java.math.BigDecimal;
import java.util.Date;

public class ScoreAlgorithm {
	
   private static double GRAVITYTHPOWER = 1.8;
   private static int T_HOURS = 2; 
   
   /**
    * @param p 分享数
    * @param date 最后分享时间
    * @return
    * @throws Exception
    */
   public static double getScore(int p,long date){
	 double numerator = 0;
	 double denominator = 0;
	 
	 long n_hours = new Date().getTime();
     long o_hours = date;	 
     long   t_hour   = (n_hours - o_hours)/3600000;
     if(t_hour==0){
       t_hour = 1;
     }
	 numerator = p;
	 denominator = Math.pow(t_hour+T_HOURS,GRAVITYTHPOWER);
     BigDecimal format = new BigDecimal(numerator/denominator);
	 return format.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	   
   }
}
