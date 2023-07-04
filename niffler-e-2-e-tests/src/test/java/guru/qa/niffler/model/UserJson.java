package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String username;

    private transient String password;

    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("friendState")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FriendState friendState;

    private transient List<UserJson> friends = new ArrayList<>();
    private transient List<UserJson> outcomeInvitations = new ArrayList<>();
    private transient List<UserJson> incomeInvitations = new ArrayList<>();
    private transient List<CategoryJson> categories = new ArrayList<>();
    private transient List<SpendJson> spends = new ArrayList<>();


    public UserJson() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public FriendState getFriendState() {
        return friendState;
    }

    public void setFriendState(FriendState friendState) {
        this.friendState = friendState;
    }

    public List<UserJson> getFriends() {
        return friends;
    }

    public void setFriends(List<UserJson> friends) {
        this.friends = friends;
    }

    public List<UserJson> getOutcomeInvitations() {
        return outcomeInvitations;
    }

    public void setOutcomeInvitations(List<UserJson> outcomeInvitations) {
        this.outcomeInvitations = outcomeInvitations;
    }

    public List<UserJson> getIncomeInvitations() {
        return incomeInvitations;
    }

    public void setIncomeInvitations(List<UserJson> incomeInvitations) {
        this.incomeInvitations = incomeInvitations;
    }

    public List<CategoryJson> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryJson> categories) {
        this.categories = categories;
    }

    public List<SpendJson> getSpends() {
        return spends;
    }

    public void setSpends(List<SpendJson> spends) {
        this.spends = spends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJson userJson = (UserJson) o;
        return Objects.equals(id, userJson.id) && Objects.equals(username, userJson.username) && Objects.equals(firstname, userJson.firstname) && Objects.equals(surname, userJson.surname) && currency == userJson.currency && Objects.equals(photo, userJson.photo) && friendState == userJson.friendState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, surname, currency, photo, friendState);
    }

    @Override
    public String toString() {
        return "UserJson{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", currency=" + currency +
                ", photo='" + photo + '\'' +
                ", friendState=" + friendState +
                ", friends=" + friends +
                '}';
    }
}