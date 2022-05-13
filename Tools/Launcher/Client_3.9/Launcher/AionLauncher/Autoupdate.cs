using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Diagnostics;
using System.Net;
using System.Threading;

namespace AionLauncher
{
    public partial class Autoupdate : Form
    {
        public Autoupdate()
        {
            InitializeComponent();
            this.Location = new System.Drawing.Point(System.Windows.Forms.SystemInformation.WorkingArea.Width - this.Width,
                                                     System.Windows.Forms.SystemInformation.WorkingArea.Height - this.Height);
            this.Opacity = 0;
            copy.Visible = false;
            progressBar1.Visible = false;
        }
        public static string setupname { get; set; }
        private void Autoupdate_Load(object sender, EventArgs e)
        {
            this.Show();
            while (this.Opacity != 1)
            {
                this.Opacity += 0.02;
                Thread.Sleep(10);
            }
            progressBar1.Visible = true;
            Uri uri = new System.Uri(Launcher.setupurl);
            Autoupdate.setupname = System.IO.Path.GetFileName(uri.LocalPath);
            WebClient client = new WebClient();
            client.DownloadProgressChanged += new DownloadProgressChangedEventHandler(client_DownloadProgressChanged);
            client.DownloadFileCompleted += new AsyncCompletedEventHandler(client_DownloadFileCompleted);
            client.DownloadFileAsync(uri, setupname);

        }
        private void client_DownloadProgressChanged(object sender, DownloadProgressChangedEventArgs e)
        {
            double bytesIn = double.Parse(e.BytesReceived.ToString());
            double totalBytes = double.Parse(e.TotalBytesToReceive.ToString());
            double percentage = bytesIn / totalBytes * 100;
            int percent = int.Parse(Math.Truncate(percentage).ToString());
            progressBar1.Value = percent;
            this.percent.Text = percent.ToString() + "%";
            this.percent.Refresh();
            progressBar1.Refresh();
        }
        private void client_DownloadFileCompleted(object sender, AsyncCompletedEventArgs e)
        {
            percent.Text = "100%";
            progressBar1.Visible = false;
            percent.Visible = false;
            copy.Visible = true;
            copy.Refresh();
            launchsetup();
        }
        private void launchsetup()
        {
            Process process = new Process();
            process.StartInfo = new ProcessStartInfo(setupname);
            process.Start();
            Thread.Sleep(3000);
            ExitSetup();
        }
        private void ExitSetup()
        {
            while (this.Opacity != 0)
            {
                this.Opacity -= 0.02;
                Thread.Sleep(10);
            }
            this.Hide();
            Application.Exit();
        }
    }
}
