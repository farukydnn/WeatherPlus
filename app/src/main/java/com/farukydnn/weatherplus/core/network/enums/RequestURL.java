package com.farukydnn.weatherplus.core.network.enums;


import java.util.Locale;

public enum RequestURL {

    BaseURL {
        @Override
        public String toString() {
             return "http://api.openweathermap.org/data/2.5/";
        }
    },

    AppId {
        @Override
        public String toString() {
            return "&APPID=3b17492d0ec2d9ff74a940f6f6c30d10";
        }
    },

    Weather {
        @Override
        public String toString() {
            return "weather?";
        }
    },

    Forecast {
        @Override
        public String toString() {
            return "forecast?";
        }
    },

    QueryQ {
        @Override
        public String toString() {
            return "q=";
        }
    },

    QueryId {
        @Override
        public String toString() {
            return "id=";
        }
    },

    QueryLat {
        @Override
        public String toString() {
            return "lat=";
        }
    },

    QueryLon {
        @Override
        public String toString() {
            return "&lon=";
        }
    },

    Units {
        @Override
        public String toString() {
            return "&units=metric";
        }
    },

    Lang {
        @Override
        public String toString() {
            return "&lang=" + Locale.getDefault().getLanguage();
        }
    }

}
