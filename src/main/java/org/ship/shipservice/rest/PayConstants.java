package org.ship.shipservice.rest;

public abstract interface PayConstants {
	public static final String DOMAIN_URI = "http://mastest.sdo.com";
	public static final String DOMAIN_URI_PROD = "https://mgw.shengpay.com/mas";
	public static final String GETINSTLIST_URI = "/api-acquire-channel/services/express/getInstList";
	public static final String PRECHECKFORSIGN_URI = "/api-acquire-channel/services/express/precheckForSign";
	public static final String CHECK_SIGN_URI = "/api-acquire-channel/services/express/sign";
	public static final String UN_SIGN_URI = "/api-acquire-channel/services/express/unsign";
	public static final String CREATEPAYMENTORDER_SIGN_URI = "/api-acquire-channel/services/express/createPaymentOrder";
	public static final String PRECHECKFORPAYMENT_SIGN_URI = "/api-acquire-channel/services/express/precheckForPayment";
	public static final String PAYMENT_URI = "/api-acquire-channel/services/express/payment";
	public static final String SNKRSA_SIGN_PRIVATEKEY = "BwIAAAAkAABSU0EyAAQAAAEAAQCrTJTlKZuEV4WLWoI2lIwdJxexLYZJSesm4lmIvRybVeoBvIDT5Ytm9zupmELzSWgAQR6AjZvy3LbTzW538CiJNCKRF3lMkqexHpzlz52bZymEFmVfT0bdPMinWfkBC6S294NKfGHCPOdZpaYfO8w3T++CPb4WxEQY/VVbivf2uoe1Rr/GOfh8I/ktvOb1ebt84UFQ4JO67qff9YIhDaanhTXkB2mZ5VnZiqMKXqaRzUEhPGQuE1QgcfabU6BpCfe9eK9+8bEougzU8J9BuO139sMGh5F6as6p2SA8BNuf94OHe8qJGDWkNiwacLKJ/jZz3ugZKE3Bd7WzuNwRlL/Bac9z73mH4x7KwYkLPEI4aI/AcfQOBd1J2o3zAIckKS2Hjz9tCK8j0cOS2mpuacx9/C4f/uRJ2wV6mQ/lJq5VND3H8mNqdFBf8QIyb15KfUzmr5lXokwAQFd63Mri3ZyVIDQypJolyx1PBfMm4ML6zl1kxl3jpENcWBl7tLHjTKhueLrb28M4XMGRC4qQlCSNFA5M4Q5T7AAAlL4Iv8sPIKE/nCyor3icm6L9uoUt8K4UQ8qRkgSN664zcm/cowmHaRXLixpNNfCHY5oJwqSdRWgKkYceFyYwYoagIvUZ9z3dCIiJfUecZOYrStiyQxxFctJG3sCosYRlBISKzRmYkgL0jUiBie72L4luYXd6bmSVl3NBgTTi7kmNvVmsZa9hM1nRRRpvMYX5pcL2enp4hRzPdUOgy1o4NUegDr73vnA=";
	public static final String RSA_SIGN_PRIVATEKEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDYXZmMYfdpEQVgFUCg8lj70EFmXrDbHNRxCBVjxdvtTUZzY33ckiuP31axZarqvuaPZOCvpjlNc0ZNamxOVBzF/qlhlNmavcvM8ZWs7xyjmm4i1apCMscWrLdEkSH3nPNBZuv2duJWkt3l/KrQvME6vfuSTFqGpNFWvWIpORkaAkKpZ2XdFJg9cQdck7CvXmsOf5UpNP/VjpBT8vZfE5PLLuI6CsUv/qywV2S+3S5NXRH3pzhq8xCZIL/lWrG06hXQ4CqUup6/xdhtMralM8IRUgjDtKBb+963QpYCuFtb5d2kaWGs6tD3gGXaryhComCFav3OgwkMoCvJUdJcPUTXAgMBAAECggEBAIe1XaPxpo8d//MeIWVR3IJFQ2AEMIWedZlX6qKj7afP+kpRsxXwEKay/NtT23pVtqNmMI+7gOGVVhkkkT4n/8woSPFNvZcTSIsJVEodyCbNrBrpTVssIjeUqXa2WUnIBcOV3JbARoLqp4ejjThTzBceJnbCsV0Wb78qFjGpAJeQf5kFhkx+BK88e5Z06C3JRbQHULmz5oLVRU48Zgcn1Jhp3ZdfKTzO9eIThq/wszcLTzRCVocx1IF66Q4UCmLE2owyR1LhZ4ZA6YioMCZPbMjAeUYPSucT8PrsNPhQZOtoF1bW5nCeAYuQuUyIQBbRNX9bbMDYj9OP7OFabuTJB2ECgYEA+jkBRITPXl6JG/9Pauz6sXFzdu6gm97y9RYHKBW9C7xc0GxIQM4OlVAkUJzIv6pOT5G+kl4T/e02VmX0ffwRp4u4Tna5n4G4Gocsqa4DcECdnvhMbqKlww/5VCv1Fvrt6Q/Rq9dmannwbiA0jcHBknHnCAaS70UDe67xj0Wqwr8CgYEA3Vx5tvYeSFLo/3Y5gcQf0f5Ruk27gx9mu0SvtLSYFjyxExqYAJo8ZwkNJYZDmCcu9J8rh8LboxYKgcFIVKMdefWb7JOHyf8wAiudZMZECt32TaEN71iDgiH9yhM2uQ05J+RINe4zj/7Gbj+FH+ijP1gNi0JOVeoj+lfJUtMQO+kCgYEAg+nFh0/U2tVPxxjDz4T7bMx4qLyIo2PYBekFANblAOjerWpIdRGskn7bhjwBgTnRaxVUuGksdPO3b7j0Oe7Hh+Ka2ZKxrSt/2Uxl+VYpreYCsqoH8VOBu+IR+ZPq86B6CCI00TkPXxbF7+i+i/UXjZLKz2pX0Bg8C9pgsr1xlpUCgYB02LWe8He3sZwwDRX5+67YSCiX8SRD6LVvsKgW+SU2x76o2ObXmpK7yLlZz2+qxzQwCD0QIrmRcrcFGyO1GY0brZwq2w1YgQ20d5VTdpzAJ7416AfVCaIRdSPkIRRHxkUfW48KeLxbDB9uXrVEzKYvb6lmkw+KpldrdB9fSu5M0QKBgQCTFBEbqMimjsf36fdEbgc4MCoqIpmnA/DCJeFiJgox1FRT4wMt9diAhLLwpdzGBfyyuZJ3eSE32eeUSE0PZyrZY9sCMCMhGNl3DtyDVmhUNrsV1+VaGBQjVObKzY3LcVy4LraOB1XVV8sZs9g2Fz6IVosIwnyla/rdBWcagaRWEg==";
	public static final String MD5_SIGN_PRIVATEKEY = "Peter1234554321Peter";
	public static final String RUN_TYPE = "prod";
	public static final String SNKRSA_SIGN_PUBLICKEY = "ACQAAASAAACUAAAABgIAAAAkAABSU0ExAAQAAAEAAQCvPKuUnRKvI8ed92Q/xoqhfFtCEDzd8wQt0M775b6egKsgRGOKEqzYI3LhNQKLnhVxOQ/0Y2V85ez2eUp1fP5WMMj8oWdqGBJDbXEQUK0jKvdDBRlWv0RB/XMSvbBBSHDZTrPckvjWn8OU6C+5uHVFSoxSNFEXfNw4JHZ93wXQpw==";
	public static final String RSA_SIGN_PUBLICKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnyHboN3r1OJP8PSBwXXpP0bbKQ60VWiJ1FzBEJ7Ek2kJXvQdTpgwgupcj2ihMW8/DCJjh7Pe/Nay6qtR9bAV1A/Wc5/WktpQvwxMeLLR5Se0a2KiShTT8kNgXg/dr7SHdnjTOHnSjpBgRUXytkcqMJstivARAWJtmzWp+sNJmj0NCjstgn48nhPrd+0yKQ+LaKpLiexpBOqmc9OYM66HnUFcczO4+NyVInTUUZ2vgbD389/H+FU03jOyAxgEqezATQrgltnoxMf3feyQF1Baq1x31oGk5skRP/WOLVmdaPsyAIU+PpyJcCO6cZzPYvzclNQq2x4W1B9FdbRz9p52WwIDAQAB";
	public static final String PROD_SIGN_PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC69veKW1X9GETEFr49gu9PN8w7H6alWec8wmF8SoP3tqQLAflZp8g83UZPX2UWhClnm53P5ZwesaeSTHkXkSI0iSjwd27N07bc8puNgB5BAGhJ80KYqTv3Zovl04C8AepVmxy9iFniJutJSYYtsRcnHYyUNoJai4VXhJsp5ZRMqwIDAQAB";
	public static final String SNKRSA_SIGN = "SNKRSA";
	public static final String RSA_SIGN = "RSA";
	public static final String MERCHANTNO = "508581";
	public static final String CHARSET = "UTF-8";
	public static final String SUCCESS = "SUCCESS";
}
