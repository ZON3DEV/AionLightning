using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing.Text;
using System.Runtime.InteropServices;
using Nini.Config;
using System.Net.Sockets;
using System.Net;
using System.Threading;
using System.Text.RegularExpressions;

namespace AionLauncher
{
    public partial class Settings : Form
    {
        public Settings()
        {
            InitializeComponent();
        }//end Constructor

        //drag zone
        public const int WM_NCLBUTTONDOWN = 0xA1;
        public const int HT_CAPTION = 0x2;

        [DllImportAttribute("user32.dll")]
        public static extern int SendMessage(IntPtr hWnd,
            int Msg, int wParam, int lParam);

        [DllImportAttribute("user32.dll")]
        public static extern bool ReleaseCapture();

        [DllImport("gdi32.dll", EntryPoint = "AddFontResourceW", SetLastError = true)]
        public static extern int AddFontResource([In][MarshalAs(UnmanagedType.LPWStr)]
        string lpFileName);
        public static int BannerCode { get; set; }
        public static bool Bannerisright { get; set; }

        private void AutoFill()
        {
            IniConfigSource launcher = new IniConfigSource("launcher.ini");
            IConfig connectionSection = launcher.Configs["Connection"];
            IConfig gameSection = launcher.Configs["Game"];
            IConfig patchSection = launcher.Configs["Patch"];
            IConfig miscSection = launcher.Configs["Misc"];
            string HOST = connectionSection.Get("IP"); //can be DNS or IP.
            string PORT = connectionSection.Get("LoginPort");
            string OPTIONS = gameSection.Get("Options");
            string LANG = gameSection.Get("Language");
            string CC = gameSection.Get("CountryCode");
            bool PATCH = patchSection.GetBoolean("Patch");
            string PATCHPATH = patchSection.Get("PatchPath");
            string PATCHVERSION = patchSection.Get("PatchVersion");
            string NEWSFEEDURL = miscSection.Get("BannerUrl");
            bool AUTOS = miscSection.GetBoolean("AutoStart");
            string LaunchLANG = miscSection.Get("LaunchLanguage");

            //set the lang selectbox
            LANG = LANG.Replace("fr", "Français");
            LANG = LANG.Replace("de", "Deutsch");
            LANG = LANG.Replace("en", "English");
            LANG = LANG.Replace("es", "Español");
            LANG = LANG.Replace("ru", "Pусский");
            LaunchLANG = LaunchLANG.Replace("fr", "Français");
            LaunchLANG = LaunchLANG.Replace("de", "Deutsch");
            LaunchLANG = LaunchLANG.Replace("en", "English");
            LaunchLANG = LaunchLANG.Replace("es", "Español");
            LaunchLANG = LaunchLANG.Replace("ru", "Pусский");
            CC = CC.Replace("1", "NA");
            CC = CC.Replace("2", "EU");
            CC = CC.Replace("7", "RU");

            //Fill form
            Lang1.Text = LaunchLANG;
            Lang2.Text = LANG;
            if (AUTOS ) { AutoTrue.Checked = true; } else { AutoFalse.Checked = true;}
            ipBox.Text = HOST;
            portBox.Text = PORT;
            extraOpts.Text = OPTIONS;
            newsUrl.Text = NEWSFEEDURL;
            patchUrl.Text = PATCHPATH;
            desiredvrs.Text = PATCHVERSION;
            CountryCode.Text = CC;
            Settings.BannerCode = 0;
            Launcher.patchvrs = PATCHVERSION;
            if (Launcher.ForceCheck )
                FUpdateBtn.Text = "Cancel update";
        }

        //Events
        protected override void OnTextChanged(System.EventArgs e)
        {
            AutoFill();
        }
        private void drag_MouseDown(object sender, System.Windows.Forms.MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                ReleaseCapture();
                SendMessage(Handle, WM_NCLBUTTONDOWN, HT_CAPTION, 0);
            }
        }
        private void CloseBtn_Click(object sender, EventArgs e)
        {
            this.Hide();
        }
        private void Cancel_Click(object sender, EventArgs e)
        {
            this.Close();
        }
        private void Reset_Click(object sender, EventArgs e)
        {
            AutoFill();
        }
        private void Confirm_Click(object sender, EventArgs e)
        {
            string LaunchLANG = Lang1.Text;
            string LANG = Lang2.Text;
            string HOST = ipBox.Text;
            int PORT = Convert.ToInt32(portBox.Text);
            string OPTIONS = extraOpts.Text;
            string NEWSFEEDURL = newsUrl.Text;
            bool PATCH = false;
            string PATCHPATH = patchUrl.Text;
            string PATCHVERSION = desiredvrs.Text;
            string CC = CountryCode.Text;
            bool AutoStart = AutoTrue.Checked;
            Launcher.patchvrs = PATCHVERSION;

            if (Settings.BannerCode == 1)
               checkBnrBtn_Click(null, null);
            else
            {

                LANG = LANG.Replace("English", "en");
                LANG = LANG.Replace("Deutsch", "de");
                LANG = LANG.Replace("Français", "fr");
                LANG = LANG.Replace("Español", "es");
                LANG = LANG.Replace("Pусский", "ru");
                LaunchLANG = LaunchLANG.Replace("English", "en");
                LaunchLANG = LaunchLANG.Replace("Deutsch", "de");
                LaunchLANG = LaunchLANG.Replace("Français", "fr");
                LaunchLANG = LaunchLANG.Replace("Español", "es");
                LaunchLANG = LaunchLANG.Replace("Pусский", "ru");
                CC = CC.Replace("NA", "1");
                CC = CC.Replace("EU", "2");
                CC = CC.Replace("RU", "7");

                IniConfigSource launcher = new IniConfigSource("launcher.ini");
                IConfig connectionSection = launcher.Configs["Connection"];
                IConfig gameSection = launcher.Configs["Game"];
                IConfig patchSection = launcher.Configs["Patch"];
                IConfig miscSection = launcher.Configs["Misc"];

                if (desiredvrs.Text != "Off") { PATCH = true; } else { PATCH = false; }
                if (PATCH) { desiredvrs.Text = PATCHVERSION; } else { desiredvrs.Text = "Off"; FUpdateBtn.Visible = false; patchUrl.Enabled = false; }

                if (FUpdateBtn.Text == "We will do..")
                    Launcher.ForceCheck = true;
                else if (FUpdateBtn.Text == "Cancelled..")
                    Launcher.ForceCheck = false;
                string CurrentNEWSFEEDURL = miscSection.Get("BannerUrl");
                if (CurrentNEWSFEEDURL != newsUrl.Text)
                    Launcher.ChangeBanner = newsUrl.Text;
                if (!Bannerisright)
                    NEWSFEEDURL = miscSection.Get("BannerUrl");
                if(LaunchLANG != miscSection.Get("LaunchLanguage")) {
                    MessageBox.Show("The launcher will restart to apply the new language", "Restart", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    Application.Restart();
                }
                connectionSection.Set("IP", HOST);
                connectionSection.Set("LoginPort", PORT);
                gameSection.Set("Options", OPTIONS);
                gameSection.Set("Language", LANG);
                gameSection.Set("CountryCode", CC);
                patchSection.Set("PatchPath", PATCHPATH);
                patchSection.Set("PatchVersion", PATCHVERSION);
                patchSection.Set("Patch", PATCH);
                miscSection.Set("BannerUrl", NEWSFEEDURL);
                miscSection.Set("AutoStart", AutoStart);
                miscSection.Set("LaunchLanguage", LaunchLANG);

                launcher.Save("launcher.ini");
                this.Hide();
            }
        }
        private void FUpdateBtn_Click(object sender, EventArgs e)
        {
            if (Launcher.ForceCheck )
            {
                bool fupdate = false;
                Launcher.ForceCheck = fupdate;
                FUpdateBtn.Text = "Cancelled..";
                FUpdateBtn.Enabled = false;
                FUpdateBtn.BackColor = Color.DarkOrange;
                FUpdateBtn.ForeColor = Color.Black;
            }
            else
            {
                FUpdateBtn.Text = "We will do..";
                FUpdateBtn.Enabled = false;
                FUpdateBtn.BackColor = Color.DarkOrange;
                FUpdateBtn.ForeColor = Color.Black;
            }
        }
        private void checkSrvBtn_Click(object sender, EventArgs e)
        {
            string HOST = ipBox.Text;
            int PORT = Convert.ToInt32(portBox.Text);
            using (var socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp))
            {
                try
                {
                    if (HOST == string.Empty | HOST == "xxx.xxx.xxx.xxx")
                        MessageBox.Show("Please make sure your ip or dns is set", "Invalid HOST", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    else
                    {
                        socket.Connect(HOST, PORT);
                        socket.Close();
                        checkSrvBtn.Text = "Succes";
                        checkSrvBtn.BackColor =  System.Drawing.Color.LimeGreen;
                        checkSrvBtn.ForeColor = System.Drawing.Color.DarkGreen;
                        checkSrvBtn.Enabled = false;
                    }
                }
                catch (SocketException)
                {
                 checkSrvBtn.Enabled = false;
                 checkSrvBtn.Text = "Error";
                 checkSrvBtn.BackColor = System.Drawing.Color.Red;
                 checkSrvBtn.ForeColor = System.Drawing.Color.DarkRed;
                 MessageBox.Show("Uhoh.. An error was return..", "Connection Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                } //end try/catch
                Thread.Sleep(1500);
                checkSrvBtn.Enabled = true;
                checkSrvBtn.BackColor = System.Drawing.Color.DimGray;
                checkSrvBtn.ForeColor = System.Drawing.Color.DimGray;
                checkSrvBtn.Text = "Check server";
            }
        }
        private void checkBnrBtn_Click(object sender, EventArgs e)
        {
            string NEWSFEEDURL = newsUrl.Text;

            if (newsUrl.Text == "" | newsUrl.Text == null)
            {
                Settings.BannerCode = -1;
                Launcher.BannerCode = -1;
                checkNewsBtn.Text = "Error";
                checkNewsBtn.BackColor = System.Drawing.Color.Red;
                checkNewsBtn.ForeColor = System.Drawing.Color.DarkRed;
                checkNewsBtn.Enabled = false;
            }
            else
            {
                try
                {
                    HttpWebRequest request = (HttpWebRequest)WebRequest.Create(NEWSFEEDURL);
                    request.Method = "HEAD";
                    request.AllowAutoRedirect = false;
                    HttpWebResponse response = (HttpWebResponse)request.GetResponse();

                    if (response.StatusCode == HttpStatusCode.OK)
                    {
                        Settings.BannerCode = 200;
                        Launcher.BannerCode = 200;
                        checkNewsBtn.Text = "Succes";
                        checkNewsBtn.BackColor = System.Drawing.Color.LimeGreen;
                        checkNewsBtn.ForeColor = System.Drawing.Color.DarkGreen;
                        checkNewsBtn.Enabled = false;
                        Bannerisright = true;
                    }
                    response.Close();
                }
                catch (WebException ex)
                {
                    if (ex.Status == WebExceptionStatus.ProtocolError)
                    {
                        var response = ex.Response as HttpWebResponse;
                        if (response != null)
                        {
                            Settings.BannerCode = (int)response.StatusCode;
                            Launcher.BannerCode = (int)response.StatusCode;
                            checkNewsBtn.Enabled = false;
                            checkNewsBtn.Text = "Error";
                            checkNewsBtn.BackColor = System.Drawing.Color.Red;
                            checkNewsBtn.ForeColor = System.Drawing.Color.DarkRed;
                            MessageBox.Show("Unable to load the page : " + (int)response.StatusCode, "Connection Error " + (int)response.StatusCode, MessageBoxButtons.OK, MessageBoxIcon.Error);
                            Settings.Bannerisright = false;
                        }
                    }
                }
                Thread.Sleep(1500);
                checkNewsBtn.Enabled = true;
                checkNewsBtn.BackColor = System.Drawing.Color.DimGray;
                checkNewsBtn.ForeColor = System.Drawing.Color.DimGray;
                checkNewsBtn.Text = "Check url";
            }
        }
        private void Lang2_SelectedValueChanged(object sender, EventArgs e)
        {
            //Set CC
            if (Lang2.Text == "English") { CountryCode.Text = "NA"; }
            if (Lang2.Text == "Deutsch" | Lang2.Text == "Español" | Lang2.Text == "Français") { CountryCode.Text = "EU"; }
            if (Lang2.Text == "Pусский") { CountryCode.Text = "RU"; }
        }
        private void newsUrl_TextChanged(object sender, EventArgs e)
        {
            Settings.BannerCode = 1;
        }

        private void desiredvrs_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (desiredvrs.Text != "Off")
            {
                FUpdateBtn.Visible = true;
                patchUrl.Enabled = true;
            }
            else
            {
                FUpdateBtn.Visible = false;
                Launcher.ForceCheck = false;
                patchUrl.Enabled = false;
            }
        }
    }
}