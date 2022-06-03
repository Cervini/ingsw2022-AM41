package it.polimi.ingsw.communication.messages;

import it.polimi.ingsw.communication.GamePack;

public class Response {
    private static boolean good;
    private static int category;
    private static int specification;
    private static GamePack status;

    public Response(int message) {

    }

    public Response(int message, GamePack status){

    }

    public static boolean isGood() {
        return good;
    }

    public static void setGood(boolean good) {
        Response.good = good;
    }

    public static int getCategory() {
        return category;
    }

    public static void setCategory(int category) {
        Response.category = category;
    }

    public static int getSpecification() {
        return specification;
    }

    public static void setSpecification(int specification) {
        Response.specification = specification;
    }

    public static GamePack getStatus() {
        return status;
    }

    public static void setStatus(GamePack status) {
        Response.status = status;
    }
}