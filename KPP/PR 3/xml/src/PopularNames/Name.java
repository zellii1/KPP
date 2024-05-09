package PopularNames;

class Name {
    private String name;
    private String gender;
    private String ethnicity;
    private int count;
    private int ranking;

    public Name() {
    }

    public Name(String name, String gender, int count, int ranking) {
        this.name = name;
        this.gender = gender;
        this.count = count;
        this.ranking = ranking;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                ", Gender: " + gender +
                ", Ethnicity: " + ethnicity +
                ", Count: " + count +
                ", Ranking: " + ranking;
    }
}