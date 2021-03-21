package com.vandeilson.APIwallet.utils;

import com.vandeilson.APIwallet.model.Users;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class UsersGroupSequenceProvider implements DefaultGroupSequenceProvider<Users> {
    @Override
    public List<Class<?>> getValidationGroups(Users users) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Users.class);

        if (IsUsersSelected(users)){
            groups.add(users.getType().getGroup());
        }

        return groups;
    }

    protected boolean IsUsersSelected(Users users) {
        return users != null && users.getType() != null;
    }
}
