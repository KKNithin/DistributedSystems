# Modifications Done:

1. Added Main() block to all the Qutation Services (Auldfellas, Dodgydrivers & Girlpower) to expose them as Jax-WS Services.
2. Modified code in Broker service to expose it to client as a Jax-WS service, Also Modified code to make Jax-WS calls to the above services to get the quotation proxys.
3. Added Main() block to client to make a Jax-WS call to broker by passing the client-info to get all the quotations and display them.

# Testing & Junits:

1. Added required junits where ever possible to get sufficient code coverage

# New Features/Enhancements:
 
1. Added code to take custom input `-m` followed with either `jmdns` or `default` to run the entire project with or without jmdns
2. In the main project folder `.env` file is present with the below config
`MODE=jmndns`

# How to Run:

1. Open a command prompt in the main folder of project and execute the command
    `docker compose up`

2. If you want to change the mode and re-run the project, modify the '.env' file and
change it to `MODE=default`

3. once done run the below command and docker will start with default mode


