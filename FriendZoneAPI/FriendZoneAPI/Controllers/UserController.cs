using FriendZoneAPI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace FriendZoneAPI.Controllers
{
    public class UserController : ApiController
    {
        FriendZoneEntities database = new FriendZoneEntities();

        // GET api/user
        public List<User> Get()
        {
            List<User> users = database.Users.ToList();
            return users;
        }

        // GET api/user/email
        public User Get(string email)
        {
            User user = database.Users.Include(u => u.User2).SingleOrDefault(u => u.Email.Equals(email));
            user.User1 = null;
            user.User2.User1 = null;
            user.User2.User2 = null;
            return user;
        }

        // POST api/user
        public void Post([FromBody]User user)
        {
            database.Users.Add(user);
            database.SaveChanges();
        }

        // DELETE api/user/email
        public void Delete(User email)
        {
            database.Users.Remove(database.Users.SingleOrDefault(u => u.Email.Equals(email)));
            database.SaveChanges();
        }
    }
}
