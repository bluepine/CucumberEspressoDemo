/*
 * Copyright (C) 2015 emmasuzuki <emma11suzuki@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.emmasuzuki.cucumberespressodemo.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.test.ActivityInstrumentationTestCase2
import android.view.View
import android.widget.EditText
import com.emmasuzuki.cucumberespressodemo.LoginActivity
import com.emmasuzuki.cucumberespressodemo.R
import cucumber.api.CucumberOptions
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.internal.matchers.TypeSafeMatcher

@CucumberOptions(features = arrayOf("features"))
class LoginActivitySteps(activityClass: LoginActivity) : ActivityInstrumentationTestCase2<LoginActivity>(LoginActivity::class.java) {

    @Given("^I have a LoginActivity")
    fun I_have_a_LoginActivity() {
        Assert.assertNotNull(activity)
    }

    @When("^I input email (\\S+)$")
    fun I_input_email(email: String) {
        onView(withId(R.id.email)).perform(typeText(email))
    }

    @When("^I input password \"(.*?)\"$")
    fun I_input_password(password: String) {
        onView(withId(R.id.password)).perform(typeText(password))
    }

    @When("^I press submit button$")
    fun I_press_submit_button() {
        onView(withId(R.id.submit)).perform(scrollTo()).perform(click())
    }

    @Then("^I should see error on the (\\S+)$")
    fun I_should_see_error_on_the_editTextView(viewName: String) {
        val viewId = if (viewName == "email") R.id.email else R.id.password
        val messageId = if (viewName == "email") R.string.msg_email_error else R.string.msg_password_error

        onView(withId(viewId)).check(matches(hasErrorText(activity.getString(messageId))))
    }

    @Then("^I should (true|false) auth error$")
    fun I_should_see_auth_error(shouldSeeError: Boolean) {
        if (shouldSeeError) {
            onView(withId(R.id.error)).check(matches(isDisplayed()))
        } else {
            onView(withId(R.id.error)).check(matches(not(isDisplayed())))
        }
    }

    private fun hasErrorText(expectedError: String): Matcher<in View> {
        return ErrorTextMatcher(expectedError)
    }

    /**
     * Custom matcher to assert equal EditText.setError();
     */
    private class ErrorTextMatcher constructor(private val mExpectedError: String) : TypeSafeMatcher<View>() {

        override fun matchesSafely(view: View): Boolean {
            if (view !is EditText) {
                return false
            }

            return mExpectedError == view.error
        }

        override fun describeTo(description: Description) {
            description.appendText("with error: " + mExpectedError)
        }
    }
}
