package com.example.fei.materialsweep.bean;

public class Versions{
        private float Versions;
        private String FileSrc;
        private String updateForce;

        public Versions() {
        }

        public Versions(int versions, String fileSrc, String updateForce) {
            Versions = versions;
            FileSrc = fileSrc;
            this.updateForce = updateForce;
        }

        public float getVersions() {
            return Versions;
        }

        public void setVersions(int versions) {
            Versions = versions;
        }

        public String getFileSrc() {
            return FileSrc;
        }

        public void setFileSrc(String fileSrc) {
            FileSrc = fileSrc;
        }

        public String getUpdateForce() {
            return updateForce;
        }

        public void setUpdateForce(String updateForce) {
            this.updateForce = updateForce;
        }

        @Override
        public String toString() {
            return "Versions{" +
                    "Versions='" + Versions + '\'' +
                    ", FileSrc='" + FileSrc + '\'' +
                    ", updateForce='" + updateForce + '\'' +
                    '}';
        }
    }