import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        ArrayList<TeamInfo> TEAMINFO = new ArrayList<>();
        TeamInfo teamInfo;

        Scanner footballData = new Scanner(new File("footballdata.txt"));

        while(footballData.hasNextLine()) {
            String inputLine = footballData.nextLine();
            teamInfo = new TeamInfo(inputLine);

            TEAMINFO.add(teamInfo);
            //System.out.println(TEAMINFO);
        }

        System.out.println(mostHomeGames(TEAMINFO));
        System.out.println(mostAwayGames(TEAMINFO));
        System.out.println(averageBrownsScore(TEAMINFO));
        System.out.println(biggestLossDate(TEAMINFO));
        System.out.println(teamWithMostLosses(TEAMINFO));

    }

    public static String mostHomeGames(ArrayList<TeamInfo> teamInfo) {
        ArrayList<String> allTeams = new ArrayList<>();
        ArrayList<String> teams = new ArrayList<>();

        boolean[] homeWins = homeWins(teamInfo);
        int[] wins;

        int mostWins = Integer.MIN_VALUE;
        String teamWithMostWins = "";

        TeamInfo temp;
        String teamAdd;

        for(int i = 0; i < teamInfo.size(); i++) { //get all teams
            temp = teamInfo.get(i);

            teamAdd = temp.getHomeTeam();
            allTeams.add(teamAdd);

            for(String team : allTeams) { //make an arraylist that doesn't contain duplicate teams
                if(!teams.contains(team)) {
                    teams.add(team);
                }
            }
        }
        wins = new int[teams.size()];
        for(int i = 0; i < allTeams.size(); i++) {

            if(homeWins[i]) {
                wins[teams.indexOf(allTeams.get(i))]++;
            }

            for(int j = 0; j < wins.length; j++) {
                if(wins[j] > mostWins) {
                    mostWins = wins[j];
                }
            }
        }
        teamWithMostWins = teams.get(findTeamIndex(wins, mostWins));
        return "the team with the most home game wins was the " + teamWithMostWins + " with " + mostWins + " home wins.";
    }

    public static String mostAwayGames(ArrayList<TeamInfo> teamInfo) {
        ArrayList<String> allTeams = new ArrayList<>();
        ArrayList<String> teams = new ArrayList<>();

        boolean[] homeWins = homeWins(teamInfo);
        int[] wins;

        int mostWins = Integer.MIN_VALUE;
        String teamWithMostWins = "";

        TeamInfo temp;
        String teamAdd;

        for(int i = 0; i < teamInfo.size(); i++) { //get all teams
            temp = teamInfo.get(i);

            teamAdd = temp.getHomeTeam();
            allTeams.add(teamAdd);

            for(String team : allTeams) { //make an arraylist that doesn't contain duplicate teams
                if(!teams.contains(team)) {
                    teams.add(team);
                }
            }
        }
        wins = new int[teams.size()];
        for(int i = 0; i < allTeams.size(); i++) {

            if(homeWins[i] == false) {
                wins[teams.indexOf(allTeams.get(i))]++;
            }

            for(int j = 0; j < wins.length; j++) {
                if(wins[j] > mostWins) {
                    mostWins = wins[j];
                }
            }
        }
        teamWithMostWins = teams.get(findTeamIndex(wins, mostWins));
        return "the team with the most away game wins was the " + teamWithMostWins + " with " + mostWins + " away wins.";
    }

    public static String averageBrownsScore(ArrayList<TeamInfo> teamInfo) {

        TeamInfo temp;
        String homeTeam = "";
        String awayTeam = "";

        int totalScore = 0;
        int totalGamesPlayed = 0;

        for(int i = 0; i < teamInfo.size(); i++) {
            temp = teamInfo.get(i);
            homeTeam = temp.getHomeTeam();
            awayTeam = temp.getAwayTeam();

            if(awayTeam.equals("Cleveland Browns")) {
                totalScore += temp.getAwayTeamScore();
                totalGamesPlayed++;
            }
            else if(homeTeam.equals("Cleveland Browns")) {
                totalScore += temp.getHomeTeamScore();
                totalGamesPlayed++;
            }

        }

        double averageScore = (totalScore / totalGamesPlayed);
        return "the average score of the Cleveland Browns was: " + averageScore;

    }

    public static String biggestLossDate(ArrayList<TeamInfo> teamInfo) {

        TeamInfo temp;
        int pointDifference = 0;
        int biggestDifference = Integer.MIN_VALUE;

        int biggestDifferenceIndex = 0;

        String date = "";

        for(int i = 0; i < teamInfo.size(); i++) {
            temp = teamInfo.get(i);
            pointDifference = temp.getBiggestScore() - temp.getSmallestScore();

            if(pointDifference > biggestDifference) {
                biggestDifference = pointDifference;
                biggestDifferenceIndex = i;
            }
        }

        date = teamInfo.get(biggestDifferenceIndex).getDate();
        return "the biggest loss was on " + date + " with a " + biggestDifference + " point loss.";

    }

    public static String teamWithMostLosses(ArrayList<TeamInfo> teamInfo) {

        TeamInfo temp;

        ArrayList<String> allTeams = new ArrayList<>();
        ArrayList<String> teams = new ArrayList<>();

        String teamAdd;

        for(int i = 0; i < teamInfo.size(); i++) { //get all teams
            temp = teamInfo.get(i);

            teamAdd = temp.getHomeTeam();
            allTeams.add(teamAdd);

            for(String team : allTeams) { //make an arraylist that doesn't contain duplicate teams
                if(!teams.contains(team)) {
                    teams.add(team);
                }
            }
        }

        int[] losses = totalLossesPerTeam(teams, teamInfo);
        int mostLosses = Integer.MIN_VALUE;

        for(int i = 0; i < losses.length; i++) {
            if(losses[i] > mostLosses) {
                mostLosses = losses[i];
            }
        }
        String teamWithMostLosses = teams.get(findTeamIndex(losses, mostLosses));

        return "the team with the most losses overall was the " + teamWithMostLosses + " with " + mostLosses + " losses."; //i knew this was correct when I saw the Browns pop up.
                                                                                                                           //it was a really sad way to finish this program.

    }

    public static boolean[] homeWins(ArrayList<TeamInfo> teamInfo) {
        boolean[] answer = new boolean[teamInfo.size()];
        for(int i = 0; i < teamInfo.size(); i++) {

            if(teamInfo.get(i).getHomeTeamScore() > teamInfo.get(i).getAwayTeamScore()) {
                answer[i] = true;
            }
            else {
                answer[i] = false;
            }

        }
        return answer;
    }

    public static int findTeamIndex(int[] arr, int input) {

        for(int i = 0; i < arr.length; i++) {
            if(arr[i] == input)
                return i;
        }
        return 0;
    }

    public static String whichTeamLost(String date, String homeTeam, String awayTeam, ArrayList<TeamInfo> teamInfo) {

        TeamInfo temp;

        for(int i = 0; i < teamInfo.size(); i++) {
            temp = teamInfo.get(i);

            if(date == temp.getDate() && homeTeam == temp.getHomeTeam() && awayTeam == temp.getAwayTeam()) {
                if(temp.getHomeTeamScore() < temp.getAwayTeamScore())
                    return homeTeam;
                else
                    return awayTeam;
            }
        }
        return "";
    }

    public static int[] totalLossesPerTeam(ArrayList<String> teams, ArrayList<TeamInfo> teamInfo) {
        TeamInfo temp;

        String date;
        String homeTeam;
        String awayTeam;

        int[] losses = new int[teams.size()];

        for(int i = 0; i < teamInfo.size(); i++) {
            temp = teamInfo.get(i);

            date = temp.getDate();
            homeTeam = temp.getHomeTeam();
            awayTeam = temp.getAwayTeam();

            if(whichTeamLost(date, homeTeam, awayTeam, teamInfo) == awayTeam) {
                losses[teams.indexOf(awayTeam)]++;
            }
            else if(whichTeamLost(date, homeTeam, awayTeam, teamInfo) == homeTeam) {
                losses[teams.indexOf(homeTeam)]++;
            }

        }

        return losses;

    }

}
