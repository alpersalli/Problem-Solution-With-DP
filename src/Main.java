import java.io.*;
import java.util.ArrayList;

public class Main {
    public static int DP(int n, int p, int c, ArrayList<Integer> salaries, ArrayList<Integer> yearDemands){

        int salarySize = salaries.size();
        int[][] costArray = new int[n+1][salarySize+1];

        for(int i = 0; i < n+1; i++){       // harcanan miktarın tutulacağı matris
            for(int k = 0; k < salarySize+1; k++){
                costArray[i][k] = -1;       // her matris elemanını -1 vererek daha sonradan bu indekslere müdahale etme
            }
        }


        for(int i = 0; i < n+1; i++){
            int demand = i==n ? 0 : yearDemands.get(i);    // demands değerini array listesinden alma

            if(i == 0){     //
                if (demand > p) {
                    costArray[0][0] = (demand - p)*c;       // eğer demand p den küçükse direkt antrenör tutup maliyetini matrise yazma
                } else {
                    for(int l=0; l <= (p-demand); l++){     // eğer demand p'den küçükse futbolcuları kullanma
                        if(l == 0) costArray[0][0] = 0;
                        else{
                            costArray[0][l] = salaries.get(l-1);
                        }
                    }
                }
            } else if(i == n){
                int k = 0;
                while ((k<salarySize+1) && (costArray[i-1][k] != -1)){         // matrisi gezerek eğer matris içinde -1 varsa direkt maliyeti atama
                    if(costArray[i][0] == -1){
                        costArray[i][0] = costArray[i-1][k];
                    }else{                                                    // eğer matrisin içinde değer varsa minimum maliyet değerini atama
                        costArray[i][0] = Math.min(costArray[i-1][k], costArray[i][0]);
                    }

                    k++;
                }
            } else {
                int k = 0;
                while ((k<salarySize+1) && (costArray[i-1][k] != -1)){
                    if (demand > (p+k)) {
                        if(costArray[i][0] == -1){                          // eğer matrisin içinde -1 varsa antrenör kiralayarak o değeri matrisin içine kaydet
                            costArray[i][0] = costArray[i-1][k] + (demand - (p+k))*c;
                        }else{                                              // eğer matrisin içinde -1'den farklı değer varsa antrenör kiralayarak maliyeti hesaplayıp minimum değeri kaydet
                            costArray[i][0] = Math.min((costArray[i-1][k] + (demand - (p+k))*c), costArray[i][0]);
                        }
                    } else {
                        for(int l=0; l <= ((p+k)-demand); l++){
                            if(l == 0){
                                if(costArray[i][0] == -1){              // matrisi gezerek eğer matris içinde -1 varsa direkt maliyeti atama
                                    costArray[i][0] = costArray[i-1][k];
                                }else{
                                    costArray[i][0] = Math.min(costArray[i-1][k], costArray[i][0]); // eğer matrisin içinde değer varsa minimum maliyet değerini atama
                                }
                            }
                            else{
                                if(costArray[i][l] == -1){      // eğer matrisin içindeki değer -1 ise oyuncu maaşı+maliyeti o matrise kaydet
                                    costArray[i][l] = costArray[i-1][k] + salaries.get(l-1);
                                }else{                          //  eğer matrisin içinde -1'den farklı bir değer varsa bu değeri oyuncu maaşı+maliyeti hesaplayıp minimum değeri matrise atama
                                    costArray[i][l] = Math.min((costArray[i-1][k] + salaries.get(l-1)), costArray[i][l]);
                                }
                            }
                        }
                    }

                    k++;
                }
            }

        }

        return costArray[n][0];
    }


    public static void main(String[] args) {

        int n=15, p=5, c=5;

        File file = new File("players_salary.txt");
        ArrayList<Integer> salaries = new ArrayList<>();
        ArrayList<Integer> yearDemands = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("j")) {
                    String[] howMuchSalary = line.split("\\s+");
                    salaries.add(Integer.parseInt(howMuchSalary[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file2 = new File("yearly_player_demand.txt"); // Replace with the actual file path
        try (FileInputStream fis = new FileInputStream(file2);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            String line2;
            while ((line2 = br.readLine()) != null) {
                if (!line2.startsWith("Y")) {
                    String[] howMuchDemand = line2.split("\\s+");
                    yearDemands.add(Integer.parseInt(howMuchDemand[1]));

                    if(yearDemands.size() == n) break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        int result = DP(n, p, c, salaries, yearDemands);
        System.out.println("Result: "+ result);

    }



}