package com.springbite.authorization_server.security;

public class JwkSet {

    private String kid;

    private String kty;

    private String n;

    private String use;

    private String e;

    private String alg;

    public JwkSet() {
    }

    public JwkSet(String kid, String kty, String n, String use, String e, String alg) {
        this.kid = kid;
        this.kty = kty;
        this.n = n;
        this.use = use;
        this.e = e;
        this.alg = alg;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getKty() {
        return kty;
    }

    public void setKty(String kty) {
        this.kty = kty;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    @Override
    public String toString() {
        return "JwkKey{" +
                "kid='" + kid + '\n' +
                ", kty='" + kty + '\n' +
                ", n='" + n + '\n' +
                ", use='" + use + '\n' +
                ", e='" + e + '\n' +
                ", alg='" + alg + '\'' +
                '}';
    }
}
