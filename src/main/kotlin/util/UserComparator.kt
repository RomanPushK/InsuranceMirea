package org.example.util

import org.example.model.User
import org.example.types.UserFields

class UserComparator(val first: UserFields, val second: UserFields, val currSort: Boolean) : Comparator<User> {
    override fun compare(o1: User, o2: User): Int {
        val firstComparison = when (first) {
            UserFields.ID -> o1.id.compareTo(o2.id)
            UserFields.FIRST_NAME -> o1.firstName.compareTo(o2.firstName)
            UserFields.LAST_NAME -> o1.lastName.compareTo(o2.lastName)
            UserFields.PASSPORT -> o1.passport.compareTo(o2.passport)
        }
        val res = if (firstComparison == 0) {
            when (second) {
                UserFields.ID -> o1.id.compareTo(o2.id)
                UserFields.FIRST_NAME -> o1.firstName.compareTo(o2.firstName)
                UserFields.LAST_NAME -> o1.lastName.compareTo(o2.lastName)
                UserFields.PASSPORT -> o1.passport.compareTo(o2.passport)
            }
        } else {
            firstComparison
        }
        if (currSort) return res
        return -res
    }
}