-- Entity 1:
INSERT INTO web_message(id, created, party_id)
VALUES('1e098e28-d9ba-459c-94c7-5508be826c08', '2021-11-21 10:05:48.198', 'fbfbd90c-4c47-11ec-81d3-0242ac130003');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('1e098e28-d9ba-459c-94c7-5508be826c08', 'key1', 'value1');

-- Entity 2:
INSERT INTO web_message(id, created, party_id)
VALUES('68cd9896-9918-4a80-bb41-e8fe0faf03f9', '2021-11-22 10:05:48.198', '262f3855-b985-4f3a-a4b8-7b9409ac9590');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('68cd9896-9918-4a80-bb41-e8fe0faf03f9', 'common-key', 'common-value');

-- Entity 3:
INSERT INTO web_message(id, created, party_id)
VALUES('3472ab5b-fca7-4eaf-8eec-8115c9710526', '2021-11-23 10:05:48.198', 'b7bd0e55-0811-4d3a-91d9-6bab7fd9ce5e');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('3472ab5b-fca7-4eaf-8eec-8115c9710526', 'common-key', 'common-value');

-- Entity 4:
INSERT INTO web_message(id, created, party_id)
VALUES('e535c9f7-c473-44f2-81d5-a8fbfcc932ea', '2021-11-24 10:05:48.198', 'b28d3b8f-ebb4-49de-bdc4-2f3d7aa6a933');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('e535c9f7-c473-44f2-81d5-a8fbfcc932ea', 'to-be-deleted', 'to-be-deleted');

-- Entity 5:
INSERT INTO web_message(id, created, party_id)
VALUES('3472ab5b-fca7-4eaf-8eec-8115c9710527', '2021-11-23 10:05:48.198', 'b7bd0e55-0811-4d3a-91d9-6bab7fd9ce5e');
INSERT INTO external_reference(parent_id, ref_key, ref_value)
VALUES('3472ab5b-fca7-4eaf-8eec-8115c9710527', 'common-key', 'common-value');
