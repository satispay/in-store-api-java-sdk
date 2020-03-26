package com.satispay.protocore;

public interface SatispayContext {
    String PATH_PROTOCOL_PREFIX = "https://";
    String PATH_ENDPOINT_ACTIVE = "authservices.satispay.com/spot/";

    String getBaseUrl();

    boolean enableLog();

    String getPublicKey();

    default String getServerCert() {
        return null;
    }

    SatispayContext PRODUCTION = new SatispayContext() {
        @Override
        public String getBaseUrl() {
            return PATH_PROTOCOL_PREFIX + PATH_ENDPOINT_ACTIVE;
        }

        @Override
        public boolean enableLog() {
            return false;
        }

        @Override
        public String getPublicKey() {
            return "-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvtDtFjcc2NbhoqJIJu7s\n" +
                    "OUUEgrli4ehOQb2KBBNCHJlj22kyYfkZfx5CzBGZt8hNQYQHR0jAoLSiGm8lcLja\n" +
                    "0D5Rt5WGl2Opgd9ubhhPxf7BTnkHEh2ZnTTI+7R55CVBPJ9xPFEcxEEhxsjChWaJ\n" +
                    "q6iv2vldOn4PbQ/ent3nn1PUYvEkXQmT21c1c4MC2X8YW958/S1XqPTlPDxDUSwq\n" +
                    "2O9L3aSqjej4vedmld+X0l5bdw9tT9YTWCGQsbzrWAidEagEpz22VxAR71ZsMYoV\n" +
                    "Pnjj5mkVf8FUTwX4iUhyrM0mVEN7Ro0VhuYeFOoxKmP1OuKey1EMx879GuWSeIZG\n" +
                    "GQIDAQAB\n" +
                    "-----END PUBLIC KEY-----";
        }
    };

    SatispayContext STAGING = new SatispayContext() {
        @Override
        public String getBaseUrl() {
            return PATH_PROTOCOL_PREFIX + "staging." + PATH_ENDPOINT_ACTIVE;
        }

        @Override
        public boolean enableLog() {
            return true;
        }

        @Override
        public String getPublicKey() {
            return "-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBITANBgkqhkiG9w0BAQEFAAOCAQ4AMIIBCQKCAQB3ywj4mFovtOHGqKY+fkeN\n" +
                    "EJVvwVPCF8uiutVr0Q48UH5U1vmpeSS03ghKpAD8fGm7pgqUfp8vkBbKNvqvJyXv\n" +
                    "DhyMFAtp6Dj8HEEuNXaBfcIIsIqHsXrHlXPUCXbolKoJk1K7Un0p2mV2r+NRQnEP\n" +
                    "+V2SnDUEbJiz/eRRH/KNhnkKipJKCoOqgiMxkmZcymxfUN4zleiENqDs0jGbO9VR\n" +
                    "Hnx8DWIJbYpFALsilDsd6gYzlQJy1x2hixYWNBS30pIDNu8+tempHuCYojz8Xre3\n" +
                    "C3rICMmsMrQELxBVuFzLeli0592wL5uI/lFPzs0cFzp6NPpW11W47IgV4HH+wl65\n" +
                    "AgMBAAE=\n" +
                    "-----END PUBLIC KEY-----";
        }
    };

    SatispayContext TEST = new SatispayContext() {
        @Override
        public String getBaseUrl() {
            return PATH_PROTOCOL_PREFIX + "test." + PATH_ENDPOINT_ACTIVE;
        }

        @Override
        public boolean enableLog() {
            return true;
        }

        @Override
        public String getPublicKey() {
            return "-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBITANBgkqhkiG9w0BAQEFAAOCAQ4AMIIBCQKCAQB3ywj4mFovtOHGqKY+fkeN\n" +
                    "EJVvwVPCF8uiutVr0Q48UH5U1vmpeSS03ghKpAD8fGm7pgqUfp8vkBbKNvqvJyXv\n" +
                    "DhyMFAtp6Dj8HEEuNXaBfcIIsIqHsXrHlXPUCXbolKoJk1K7Un0p2mV2r+NRQnEP\n" +
                    "+V2SnDUEbJiz/eRRH/KNhnkKipJKCoOqgiMxkmZcymxfUN4zleiENqDs0jGbO9VR\n" +
                    "Hnx8DWIJbYpFALsilDsd6gYzlQJy1x2hixYWNBS30pIDNu8+tempHuCYojz8Xre3\n" +
                    "C3rICMmsMrQELxBVuFzLeli0592wL5uI/lFPzs0cFzp6NPpW11W47IgV4HH+wl65\n" +
                    "AgMBAAE=\n" +
                    "-----END PUBLIC KEY-----";
        }

        @Override
        public String getServerCert() {
            return "-----BEGIN CERTIFICATE-----\n" +
                    "MIIE0DCCA7igAwIBAgIJAIWozCAWiOn0MA0GCSqGSIb3DQEBBQUAMIGgMQswCQYD\n" +
                    "VQQGEwJJVDEOMAwGA1UECBMFSXRhbHkxDjAMBgNVBAcTBU1pbGFuMREwDwYDVQQK\n" +
                    "EwhTYXRpc3BheTEnMCUGA1UECxMeU2F0aXNwYXkgQ2VydGlmaWNhdGUgQXV0aG9y\n" +
                    "aXR5MRQwEgYDVQQDEwtTYXRpc3BheSBDQTEfMB0GCSqGSIb3DQEJARYQaW5mb0Bz\n" +
                    "YXRpc3BheS5pdDAeFw0xMzExMDExNTA5NTBaFw0yMzEwMzAxNTA5NTBaMIGgMQsw\n" +
                    "CQYDVQQGEwJJVDEOMAwGA1UECBMFSXRhbHkxDjAMBgNVBAcTBU1pbGFuMREwDwYD\n" +
                    "VQQKEwhTYXRpc3BheTEnMCUGA1UECxMeU2F0aXNwYXkgQ2VydGlmaWNhdGUgQXV0\n" +
                    "aG9yaXR5MRQwEgYDVQQDEwtTYXRpc3BheSBDQTEfMB0GCSqGSIb3DQEJARYQaW5m\n" +
                    "b0BzYXRpc3BheS5pdDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJlE\n" +
                    "nZyYEKD20zi9eakOoPMlbLEw1H6EN7BV2drjb4qbcxDFTxgskGELTnxKVt0Q1+sK\n" +
                    "SxEMCiv1xB/n5IuspSI6TclR81MbNASj1S4lM/HUOrgxtzd7p0RKTLJuSs7YiQXl\n" +
                    "MtRiV/U3usgVUsAFg0XziQVeTNhnCOkmt3b1qunRbyRVvkXx3iE3HhwOX5x5iol2\n" +
                    "Ylzee3++/Rxz7HyY0e1gYP+5JVR80ca+WYc1vmX3GD+KCOaylA7K3ZCdVE68zPZ9\n" +
                    "MKNHUtSrQlzM0/ebVUDd1FqoM0PWTKaOY8tinxqB/haONTC/rA2J07cpi+IdKT0J\n" +
                    "WZfveYfKYK0gPnp3+ksCAwEAAaOCAQkwggEFMB0GA1UdDgQWBBQv9/10+q1Nud3U\n" +
                    "s7WqMgUQbfAtvTCB1QYDVR0jBIHNMIHKgBQv9/10+q1Nud3Us7WqMgUQbfAtvaGB\n" +
                    "pqSBozCBoDELMAkGA1UEBhMCSVQxDjAMBgNVBAgTBUl0YWx5MQ4wDAYDVQQHEwVN\n" +
                    "aWxhbjERMA8GA1UEChMIU2F0aXNwYXkxJzAlBgNVBAsTHlNhdGlzcGF5IENlcnRp\n" +
                    "ZmljYXRlIEF1dGhvcml0eTEUMBIGA1UEAxMLU2F0aXNwYXkgQ0ExHzAdBgkqhkiG\n" +
                    "9w0BCQEWEGluZm9Ac2F0aXNwYXkuaXSCCQCFqMwgFojp9DAMBgNVHRMEBTADAQH/\n" +
                    "MA0GCSqGSIb3DQEBBQUAA4IBAQCAnHtttWjESTe+6gA3qxhhNo7nUMKxO6cMCV+F\n" +
                    "+119J/zpokSkav2ABd4caGR2fECbXSpBbbHoLxPr+nifUyyxJ0MvBMqhSY3plYDX\n" +
                    "/7r/BXrRMhtmJkOOkPTxqSgY7fNQxJ1aMg2YwKecTgn84ES5fr/90CHNMXPOsCZD\n" +
                    "GRwRrgcNZQoHZ7v9w7vmxudbPFYXD83o6huVExODIQkizLieRyaJphPbkYLONoD8\n" +
                    "OUrqEh7uWP4/BHQ4f/wgp+h+hxrltDsRiOD6XuzHHazLcKp/IKAVCVN97lQLBB5I\n" +
                    "Z5DzmPIRP+bVxH21YpZG6usGNq2b3KGqpOY5mdWWZWvgwHx1\n" +
                    "-----END CERTIFICATE-----";
        } 
    };
}
