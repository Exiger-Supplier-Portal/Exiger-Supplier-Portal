import React from "react";
import { Button } from "../ui/button";

function SignInButton() {
  // const handleSignIn = () => {
  //   window.location.href = "http://localhost:8080/oauth2/authorization/okta";
  // };

  // return <Button onClick={handleSignIn}>Sign in with Okta SSO</Button>;

  return (
    <a href="http://localhost:8080/oauth2/authorization/okta">
      <Button>Sign in with Okta SSO</Button>
    </a>
  );
}

export default SignInButton;
