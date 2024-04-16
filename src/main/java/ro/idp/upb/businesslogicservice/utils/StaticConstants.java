/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticConstants {
	public final String ioServiceUrl;
	public final String authServiceUrl;

	public final String authUsersMeEndpoint;

	public final String ioUsersEndpoint;
	public final String ioFindByEmailEndpoint;
	public final String ioPostManagerEndpoint;

	public StaticConstants(
			@Value("${idp.io-service.url}") String ioServiceUrl,
			@Value("${idp.auth-service.url}") String authServiceUrl,
			@Value("${idp.auth-service.users-me-endpoint}") String authUsersMeEndpoint,
			@Value("${idp.io-service.users-endpoint}") String ioUsersEndpoint,
			@Value("${idp.io-service.find-by-email-endpoint}") String ioFindByEmailEndpoint,
			@Value("${idp.io-service.post-manager-endpoint}") String ioPostManagerEndpoint) {
		this.ioServiceUrl = ioServiceUrl;
		this.authServiceUrl = authServiceUrl;
		this.authUsersMeEndpoint = authUsersMeEndpoint;
		this.ioUsersEndpoint = ioUsersEndpoint;
		this.ioFindByEmailEndpoint = ioFindByEmailEndpoint;
		this.ioPostManagerEndpoint = ioPostManagerEndpoint;
	}
}
