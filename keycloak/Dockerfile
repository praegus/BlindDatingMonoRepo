FROM quay.io/keycloak/keycloak:26.2.5

COPY blinddating-realm.json /opt/keycloak/data/import/blinddating-realm.json

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
CMD ["start", "--import-realm"]