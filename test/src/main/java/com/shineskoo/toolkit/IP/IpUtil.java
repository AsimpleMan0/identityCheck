package com.shineskoo.toolkit.IP;

/*
 *   @Author: Cosmos
 *   @Date: 2021/1/16 2:48 下午
 */

import javax.servlet.http.HttpServletRequest;

/**
 * 获取IP地址的工具类
 */
public class IpUtil {
    /**
     * 私有化构造器
     */
    private IpUtil() {

    }

    /**
     * 判断IP是否是内网IP
     * @param ipAddress IP地址
     * @return true/false
     */
    public static boolean isInnerIP(String ipAddress) {
        boolean isInnerIp;
        long ipNum = getIpNum(ipAddress);
        /**
         私有IP：A类  10.0.0.0-10.255.255.255
         B类  172.16.0.0-172.31.255.255
         C类  192.168.0.0-192.168.255.255
         当然，还有127这个网段是环回地址
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");

        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");

        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
                || ipAddress.equals("127.0.0.1");
        return isInnerIp;
    }

    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }

    /**
     * 获取真实IP地址
     * @param request
     * @return
     */
    public static String getRealIP(HttpServletRequest request){
        // 获取客户端ip地址
        String clientIp = request.getHeader("x-forwarded-for");

        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        String[] clientIps = clientIp.split(",");
        if(clientIps.length <= 1) {
            return clientIp.trim();
        }

        // 判断是否来自CDN
        if(isComefromCDN(request)){
            if(clientIps.length>=2) {
                return clientIps[clientIps.length-2].trim();
            }
        }

        return clientIps[clientIps.length-1].trim();
    }

//    public static String getClientIp(HttpServletRequest req) {
//        String ip = req.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = req.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = req.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = req.getRemoteAddr();
//        }
//        return ip;
//    }

    private static boolean isComefromCDN(HttpServletRequest request) {
        String host = request.getHeader("host");
        return host.contains("www.189.cn") ||host.contains("shouji.189.cn") || host.contains(
                "image2.chinatelecom-ec.com") || host.contains(
                "image1.chinatelecom-ec.com");
    }
}
