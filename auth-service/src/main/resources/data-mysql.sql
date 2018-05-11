-- INSERT INTO users (username,password,mobile, enabled) values ('restUser','1234','136819027',1);
-- INSERT INTO users (username,password,mobile, enabled) values ('restUser2','1234', '13816601773', 1);

INSERT INTO oauth_client_details
 (client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	("topBssClient", "client@topbaby","read",
	"password,authorization_code,refresh_token", null, null, 36000, 36000, null, true),
    ("resourceClient", "resourceSecret","read",
	"password,authorization_code,refresh_token", null, null, 36000, 36000, null, true);
-- INSERT INTO oauth_client_details
-- 	(client_id, client_secret, scope, authorized_grant_types,
-- 	web_server_redirect_uri, authorities, access_token_validity,
-- 	refresh_token_validity, additional_information, autoapprove)
-- VALUES
-- 	("sampleClientId", "secret", "read,write,foo,bar",
-- 	"implicit", null, null, 36000, 36000, null, false);
-- INSERT INTO oauth_client_details
-- 	(client_id, client_secret, scope, authorized_grant_types,
-- 	web_server_redirect_uri, authorities, access_token_validity,
-- 	refresh_token_validity, additional_information, autoapprove)
-- VALUES
-- 	("barClientIdPassword", "secret", "bar,read,write",
-- 	"password,authorization_code,refresh_token", null, null, 36000, 36000, null, true);