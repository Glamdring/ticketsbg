package com.tickets.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * This handles purchasing of tickets
 * It has session scope, which means it will persist throughout
 * the whole session, that is, while the user is active
 *
 * @author Bozhidar Bozhanov
 *
 */
@Controller("cartController")
@Scope("session")
public class CartController extends BaseController {

}
