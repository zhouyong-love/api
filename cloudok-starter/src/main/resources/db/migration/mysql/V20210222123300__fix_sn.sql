
update base_company set sn = id-4387233486537228800 where id <= 4387233486537228804;

update base_company set sn = mod(id,10000)+10000 where id > 4387233486537228804;

update base_industry set sn = id-4387233486537228358 + 10 where id > 4387233486537228358;

update base_industry set sn = id-4387207810404843523 where id < 4387233486537228358;

update base_job set sn = mod(id,10000)+10000;

update base_specialism set sn = id-4387233486537228292;

update base_tag set sn = id-4387233486537218361 where id < 4392356224645988352;