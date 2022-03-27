drop table analysis.issues;
drop table analysis.report;
create table analysis.issues (id  serial not null, category varchar(255), column_end int4 not null, column_start int4 not null, description varchar(255), file_name varchar(255), fingerprint varchar(255), line_end int4 not null, line_start int4 not null, message varchar(1024), module_name varchar(255), origin varchar(255), origin_name varchar(255), package_name varchar(255), reference varchar(255), severity varchar(255), type varchar(255), issues_id int4, primary key (id));
create table analysis.report (id  serial not null, origin_report_file varchar(255), tool_id varchar(255), tool_name varchar(255), primary key (id));
alter table analysis.issues add constraint FKk0dfx57gfkq6nrj48qsnqd6rp foreign key (issues_id) references analysis.report;
