package pl.edu.wat.wcy.epistimi.user.domain

class UserNotFoundException : Exception("User was not found")

class UsernameAlreadyInUseException(username: String) : Exception("User with username $username already registered")
