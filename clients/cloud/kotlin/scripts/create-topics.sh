echo "Waiting for Kafka to come online..."

cub kafka-ready -b kafka:9092 1 20

# create topics
kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_processing_address_verification \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_order \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_resale_order \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_resale_order_proto \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_result_resale_order_command \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_resale_order_retry_fail \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_processing_commission_rate \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_processing_payouts \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_cx_fact_seller \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_seller_tier \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_address_verification \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_resale_address_verification \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_resale_invoice \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_invoice_create \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_invoice_fail \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_catalog_refinement_rules \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_catalog_refinement_queries \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_payment_transaction \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_payment_transaction_order_enrichment \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_payment_transaction_order_enrichment_fail \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_invoice_administer_fail \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_cx_fact_cart \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_cx_fact_checkout \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu-cx-cmd-carrier-tracking-bulk \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_cx_fact_offer \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu-rms-cmd-resale-payout-request \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_cx_fact_payment_transaction \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_resale_tibco_invoice \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_tibco_invoice_fail \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_resale_dispute \
    --replication-factor 1 \
    --partitions 1 \
    --config cleanup.policy=compact \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_cohort_definition \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic test_topic \
    --replication-factor 1 \
    --partitions 1 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_cmd_resale_inventory \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_fact_resale_inventory \
    --replication-factor 1 \
    --partitions 4 \
    --config cleanup.policy=compact \
    --create

kafka-topics \
  --bootstrap-server kafka:29092 \
  --topic nu_cx_fact_enriched_payment_transaction \
  --replication-factor 1 \
  --partitions 1 \
  --create

kafka-topics \
  --bootstrap-server kafka:29092 \
  --topic nu_rms_fact_invoicing_enriched_payment_transaction \
  --replication-factor 1 \
  --partitions 1 \
  --create

kafka-topics \
  --bootstrap-server kafka:29092 \
  --topic nu_cx_fact_payment_transaction_complete \
  --replication-factor 1 \
  --partitions 1 \
  --create

kafka-topics \
  --bootstrap-server kafka:29092 \
  --topic nu_rms_fact_resale_invoice_summary \
  --replication-factor 1 \
  --partitions 1 \
  --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_rms_result_resale_inventory \
    --replication-factor 1 \
    --partitions 4 \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_cx_fact_listing_count \
    --replication-factor 1 \
    --partitions 4 \
    --config cleanup.policy=compact \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_cx_fact_payment_method_used \
    --replication-factor 1 \
    --partitions 4 \
    --config cleanup.policy=compact \
    --create

kafka-topics \
    --bootstrap-server kafka:29092 \
    --topic nu_fx_fact_address_used \
    --replication-factor 1 \
    --partitions 4 \
    --config cleanup.policy=compact \
    --create

sleep infinity
