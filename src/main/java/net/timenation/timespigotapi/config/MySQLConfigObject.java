package net.timenation.timespigotapi.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by EinsLuca
 * Class create at 17.07.2022 19:02 @TimeSpigotAPI
 **/
@AllArgsConstructor
@Getter
@Setter
public class MySQLConfigObject {

    public String user;
    public String password;
    public String database;
    public String host;

}
