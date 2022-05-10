-- Add New Ranks to Legion Members Table
ALTER TABLE legion_members
MODIFY `rank` enum('BRIGADE_GENERAL','SUB_GENERAL','CENTURION','LEGIONARY','NEW_LEGIONARY') NOT NULL default 'NEW_LEGIONARY';

-- Clear old Permissions to Prepare for Update, BG will need to reassign Permissions
UPDATE legions SET legionar_permission2 = 0;
UPDATE legions SET centurion_permission1 = 0;
UPDATE legions SET centurion_permission2 = 0;

-- Update Legions Table
ALTER TABLE legions
ADD `deputy_permission1` int(1) NOT NULL default '0' AFTER `contribution_points`,
ADD `deputy_permission2` int(1) NOT NULL default '0' AFTER `deputy_permission1`,
ADD `legionary_permission1` int(1) NOT NULL default '0' AFTER `deputy_permission2`,
CHANGE `legionar_permission2` `legionary_permission2` int(1) NOT NULL default '0',
MODIFY `centurion_permission1` int(1) NOT NULL default '0',
MODIFY `centurion_permission2` int(1) NOT NULL default '0',
ADD `volunteer_permission1` int(1) NOT NULL default '0' AFTER `centurion_permission2`,
ADD `volunteer_permission2` int(1) NOT NULL default '0' AFTER `volunteer_permission1`;
