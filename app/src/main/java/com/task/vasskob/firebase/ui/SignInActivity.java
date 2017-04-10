package com.task.vasskob.firebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.model.User;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener, FacebookCallback<LoginResult> {

    private static final String TAG = "SignInActivity";
    private static final String SIGN_IN_FAILED = "Sign In Failed";
    private static final String SIGN_UP_FAILED = "Sign Up Failed. Check email validity";
    private static final String EMAIL_WARN = "Pass valid email";
    private static final String PASSWORD_LENGTH_WARN = "Password must be at least 6 characters long";
    private static final String GOOGLE_PLAY_SERVICES_ERROR = "Google Play Services error.";
    private static final String AUTHENTICATION_FAILED = "Authentication failed.";
    private static final int RC_SIGN_IN = 1;
    public static final String EMAIL = "email";
    public static final String PUBLIC_PROFILE = "public_profile";
    private String username;
    private String userEmail;

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    @Bind(R.id.field_email)
    EditText mEmailField;

    @Bind(R.id.field_password)
    EditText mPasswordField;

    @OnClick(R.id.button_sign_in)
    public void onEmailSignInClick() {
        signInEmail();
    }

    @OnClick(R.id.button_sign_up)
    public void onEmailSignUpClick() {
        signUpEmail();
    }

    @OnClick(R.id.g_plus_sign_in)
    public void onGSignInClick() {
        signInGPlus();
    }

    @OnClick(R.id.facebook_sign_in)
    public void onFSignInClick() {
        singInFacebook();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        mAuth = FirebaseOperations.signInRef();

        initGPlusSignIn();
        initFacebookSignIn();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check auth on Activity start
//        if (mAuth.getCurrentUser() != null) {
//            onAuthSuccess(mAuth.getCurrentUser());
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void signInEmail() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {

                            Toast.makeText(SignInActivity.this, SIGN_IN_FAILED,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUpEmail() {
        Log.d(TAG, "signUpEmail");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(SignInActivity.this, SIGN_UP_FAILED,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        User newUser = new User(username, user.getEmail());

        // Write new user
        FirebaseOperations.CreateNewUser(newUser);

        // Go to MainActivity
        startActivity(new Intent(SignInActivity.this, LoadFileFromStorage.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        String email = mEmailField.getText().toString();
        if (!isValidEmail(email)) {
            mEmailField.setError(EMAIL_WARN);
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (mPasswordField.getText().length() < 6) {
            mPasswordField.setError(PASSWORD_LENGTH_WARN);
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    // G+ Authentication Section
    private void signInGPlus() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initGPlusSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, GOOGLE_PLAY_SERVICES_ERROR, Toast.LENGTH_SHORT).show();
            }
        } else {
            // For facebook log in callback
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        sighInWithCred(credential);

        username = acct.getDisplayName();
        userEmail = acct.getEmail();

    }

    private void sighInWithCred(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, AUTHENTICATION_FAILED,
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            onAuthGPlusSuccess(task.getResult().getUser());
                            startActivity(new Intent(SignInActivity.this, LoadFileFromStorage.class));
                            finish();
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void onAuthGPlusSuccess(FirebaseUser user) {
        Log.d(TAG, "onAuthGPlusSuccess UserName = " + user.getEmail());

        User newUser = new User(username, userEmail);
        FirebaseOperations.CreateNewUser(newUser);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, GOOGLE_PLAY_SERVICES_ERROR, Toast.LENGTH_SHORT).show();
    }

    // Facebook Authentication Section
    private void initFacebookSignIn() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, this);
    }

    private void singInFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(EMAIL, PUBLIC_PROFILE));
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Log.d(TAG, "onSuccess:" + loginResult.getAccessToken());
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onError(FacebookException e) {
        Log.d(TAG, "onError:" + e);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        sighInWithCred(credential);
    }
}
