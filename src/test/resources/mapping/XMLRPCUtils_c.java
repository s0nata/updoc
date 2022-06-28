 private static String discoverSelfHostedXmlrpcUrl(String siteUrl, String httpUsername, String httpPassword) throws
            XMLRPCUtilsException {
			
        // Array of Strings that contains the URLs we want to try
        
		
		
        final Set<String> urlsToTry = new LinkedHashSet<>();

        // add the url as provided by the user
        urlsToTry.add(siteUrl);
		}