package org.exemple.clinica.domain;

    public class Paciente {

        private Long id;
        private String nome;
        private String email;
        private String telefone;
        private String cpf;
        private Endereco endereco;

        public Paciente(Long id, boolean ativo, Endereco endereco, String cpf, String telefone, String email, String nome) {

        }

        public Paciente() {
            
        }

        public Paciente(String cpf, String nome, String email, String telefone, boolean ativo) {
        }

        public boolean isAtivo() {
            return ativo;
        }

        public void setAtivo(boolean ativo) {
            this.ativo = ativo;
        }

        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }

        public Endereco getEndereco() {
            return endereco;
        }

        public void setEndereco(Endereco endereco) {
            this.endereco = endereco;
        }

        private boolean ativo = true;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

    }
