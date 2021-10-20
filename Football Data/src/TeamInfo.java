public class TeamInfo {

    private String date;
    private String homeTeam;
    private String awayTeam;
    private int homeTeamScore;
    private int awayTeamScore;

    //setters and getters//
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public int getBiggestScore() {
        if(homeTeamScore > awayTeamScore)
            return homeTeamScore;
        else
            return awayTeamScore;
    }

    public int getSmallestScore() {
        if(homeTeamScore < awayTeamScore)
            return homeTeamScore;
        else
            return awayTeamScore;
    }
    //////////////////////

    public TeamInfo() {
        setDate("");
        setHomeTeam("");
        setAwayTeam("");
        setHomeTeamScore(0);
        setAwayTeamScore(0);
    }

    public TeamInfo(String inputLine) {
        String[] lineContents = inputLine.split(",");
        setDate(lineContents[0]);
        setHomeTeam(lineContents[1]);
        setAwayTeam(lineContents[2]);
        setHomeTeamScore(toInt(lineContents[3]));
        setAwayTeamScore(toInt(lineContents[4]));
    }

    public int toInt(String input) {
        return Integer.valueOf(input);
    }

    public String toString() {
        return date + "," + homeTeam + "," + awayTeam + "," + homeTeamScore + "," + awayTeamScore;
    }


}
