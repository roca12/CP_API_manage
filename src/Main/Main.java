

package Main;

import ApiConn.UVaAPI;
import java.io.IOException;


public class Main {

    
    public static void main(String[] args) throws IOException {
        UVaAPI.EstadisticasCompletas a= UVaAPI.getJSONUVa("694550");
        System.out.println(a.toString());
    }

}
